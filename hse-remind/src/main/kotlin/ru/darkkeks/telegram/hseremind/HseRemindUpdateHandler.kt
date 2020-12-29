package ru.darkkeks.telegram.hseremind

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.api.*
import ru.darkkeks.telegram.core.createLogger

@Component
class HseRemindUpdateHandler(
        val telegram: Telegram,
        val telegramFiles: TelegramFiles,
        val userRepository: UserRepository,
        val userConfigService: UserConfigService
) : UpdateHandler {

    private val logger = createLogger<HseRemindUpdateHandler>()

    override fun handle(update: Update) {
        when {
            update.message != null -> {
                val message = update.message!!

                if (message.chat.type != ChatType.PRIVATE) {
                    return
                }

                val document = message.document
                if (document != null) {
                    try {
                        importDocument(message.chat.id, document)
                    } catch (e: Exception) {
                        logger.error("Document import failed", e)
                        telegram.sendMessage(message.chat.id, """
                            Не получилось импортировать файлик. 😕 Напиши @darkkeks
                            $e
                        """.trimIndent()).executeChecked()
                    }
                    return
                }

                val text = message.text
                if (text == null) {
                    cantProcess(message.chat.id)
                    return
                }

                if (text.startsWith("/")) {
                    val command = text.drop(1).split(" ")
                    when (command.firstOrNull()) {
                        "start", "help" -> help(message.chat.id)
                        "import" -> {
                            import(message)
                        }
                        "export" -> {
                            val format = command.getOrNull(1) ?: "yaml"
                            export(message.chat.id, format)
                        }
                        else -> cantProcess(message.chat.id)
                    }
                    return
                }

                cantProcess(message.chat.id)
            }
        }
    }

    private fun import(message: Message) {
        telegram.sendMessage(message.chat.id, """
            Скинь конфиг файликом. Пример конфига <a href="$exampleConfig">тут</a>.
        """.trimIndent(), parseMode = ParseMode.HTML, disableWebPagePreview = true).executeChecked()
    }

    private fun importDocument(chatId: Long, document: Document) {
        val fileId = document.fileId
        val file = telegram.getFile(fileId).executeChecked()

        val size = file.fileSize
        if (size == null) {
            logger.warn("No file size: {}", file)
            return
        }

        if (size > 1 shl 20) { // 1mb
            val kb = size / (1 shl 10)
            telegram.sendMessage(chatId, """
                    Какой-то большой файлик (`${kb}kb > 1mb`) 🤔
                    Это точно конфиг?
                """.trimIndent(), parseMode = ParseMode.MARKDOWN_V2).executeChecked()
            return
        }

        val path = file.filePath
        if (path == null) {
            telegram.sendMessage(chatId, """
                    Почему-то у файла нету `file_path`. Напиши @darkkeks, или попробуй перезалить конфиг.
                """.trimIndent(), parseMode = ParseMode.MARKDOWN_V2).executeChecked()
            return
        }

        val response = telegramFiles.downloadFile(path).execute()

        val body = response.body()
        if (response.isSuccessful && body != null) {
            validateConfig(chatId, body.string())
        } else {
            throw TelegramClientException("Failed to download file: ${response.message()}")
        }
    }

    private fun validateConfig(chatId: Long, content: String) {
        val config = try {
            readMapper.readValue(content, UserSpec::class.java)
        } catch (e: Exception) {
            telegram.sendMessage(chatId, """
                Не получилось спарсить конфиг.
                Возможно тебе поможет ошибка: $e
            """.trimIndent()).executeChecked()
            return
        }

        userConfigService.updateUserConfig(chatId, config)

        telegram.sendMessage(chatId, """
            Успешно сохранил новый конфиг! 👍
        """.trimIndent()).executeChecked()
    }

    private fun export(chatId: Long, format: String?) {
        val (mapper, mimeType) = when (format) {
            "json" -> jsonWriteMapper to "application/json"
            "yaml", "yml" -> readMapper to "application/x-yaml"
            else -> null to null
        }

        if (mapper == null || mimeType == null) {
            telegram.sendMessage(chatId, """Я не умею экспортировать в формат `$format` 😟""",
                    parseMode = ParseMode.MARKDOWN_V2).executeTelegram()
            return
        }

        val userOptional = userRepository.findById(chatId)
        if (userOptional.isEmpty) {
            telegram.sendMessage(chatId, """
                Похоже у тебя еще нету конфига.
                Ты можешь создать его по примеру <a href="$exampleConfig">отсюда</a>, а потом импортировать с помощью <b>/import</>. 😉
            """.trimIndent(), parseMode = ParseMode.HTML, disableWebPagePreview = true).executeTelegram()
            return
        }

        val user = userOptional.get()
        val spec = safeParseSpec(user.spec)

        val content = try {
            mapper.writeValueAsString(spec)
        } catch (e: Exception) {
            logger.error("Cant serialize config for user {}", chatId, e)
            telegram.sendMessage(chatId, """
                Странно, не получилось экспортировать конфиг. 🤯
                Напиши пожалуйста @darkkeks. Твой репорт поможет найти проблему сразу, а не через inf дней.
            """.trimIndent(), parseMode = ParseMode.MARKDOWN_V2).executeTelegram()
            return
        }

        try {
            val document = MultipartBody.Part.createFormData("document", "config.$format",
                    RequestBody.create(MediaType.parse(mimeType), content))
            telegram.sendDocument(document, chatId, caption = """
                Держи!
            """.trimIndent()).executeChecked()
        } catch (e: Exception) {
            logger.error("Cant send exported config for user {}, config:" , chatId, content, e)
            telegram.sendMessage(chatId, """
                Странно, не получилось отправить файл с конфигом. 🤯
                Напиши пожалуйста @darkkeks. Твой репорт поможет найти проблему сразу, а не через inf дней.
            """.trimIndent(), parseMode = ParseMode.MARKDOWN_V2).executeTelegram()
        }
    }

    private val exampleConfig = "https://github.com/DarkKeks/telegram-bots/blob/master/hse-remind/src/main/resources/example_config.yml"

    fun help(chatId: Long) {
        telegram.sendMessage(chatId, """
            Привет!
            
            Я умею отправлять нотификации о парах, когда они начинаются. Пока-что конфигурировать нотификации можно только через json/yaml. Пример конфига есть <a href="$exampleConfig">тут</a>.
            
            Если хочется конфигурировать кнопочками в боте, пинайте @darkkeks.

            Команды:
            <b>/export</b> [json/yaml] — получить текущий конфиг. Формат по умолчанию — yaml.
            <b>/import</b> — Импортировать конфиг. Формат всегда yaml, так как json — это подмножество yaml.
            <b>/start</b>, <b>/help</b> — это сообщение.
        """.trimIndent(), parseMode = ParseMode.HTML, disableWebPagePreview = true).executeChecked()
    }

    fun cantProcess(chatId: Long) {
        telegram.sendMessage(chatId, """Не понимаю 😧""").executeChecked()
    }
}
