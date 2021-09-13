package ru.darkkeks.telegram.hseremind.handler

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.Context
import ru.darkkeks.telegram.core.HandlerFactory
import ru.darkkeks.telegram.core.MAIN_STATE
import ru.darkkeks.telegram.core.api.Document
import ru.darkkeks.telegram.core.api.ParseMode
import ru.darkkeks.telegram.core.api.Telegram
import ru.darkkeks.telegram.core.api.TelegramClientException
import ru.darkkeks.telegram.core.api.TelegramFiles
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.core.fromJson
import ru.darkkeks.telegram.core.handlerList
import ru.darkkeks.telegram.core.unwrap
import ru.darkkeks.telegram.core.withState
import ru.darkkeks.telegram.hseremind.UserConfigService
import ru.darkkeks.telegram.hseremind.UserRepository
import ru.darkkeks.telegram.hseremind.UserSpec
import ru.darkkeks.telegram.hseremind.jsonWriteMapper
import ru.darkkeks.telegram.hseremind.readMapper
import ru.darkkeks.telegram.hseremind.safeParseSpec

@Component
class MainHandlers(
    private val telegram: Telegram,
    private val telegramFiles: TelegramFiles,
    private val userRepository: UserRepository,
    private val userConfigService: UserConfigService,
): HandlerFactory {

    private val logger = createLogger<MainHandlers>()

    private val exampleConfig = "https://github.com/DarkKeks/telegram-bots/blob/master/hse-remind/src/main/resources/example_config.yml"

    override fun handlers() = handlerList {
        withState(MAIN_STATE) {
            command("start", this@MainHandlers::help)
            command("help", this@MainHandlers::help)

            command("chat_id") { context ->
                val chat = context.message.chat
                telegram.sendMessage(chat.id, """
                id этого чата: <pre>${chat.id}</pre>
            """.trimIndent(), parseMode = ParseMode.HTML)
            }

            command("import") { context ->
                telegram.sendMessage(context.message.chat.id, """
                Скинь конфиг файликом. Пример конфига <a href="$exampleConfig">тут</a>.
            """.trimIndent(), parseMode = ParseMode.HTML, disableWebPagePreview = true)
            }

            command("export") { context ->
                export(context.message.chat.id, format = context.args.firstOrNull())
            }

//            command("show") { context ->
//                val user = userRepository.findById(context.user.uid).unwrap()
//                if (user == null) {
//                    noConfig(context.message.chat.id)
//                } else {
//
//                }
//            }

            document { context ->
                try {
                    importDocument(context.message.chat.id, context.message.document!!)
                } catch (e: Exception) {
                    logger.error("Document import failed", e)
                    telegram.sendMessage(context.message.chat.id, """
                    Не получилось импортировать файлик. 😕 Напиши @darkkeks
                    $e
                """.trimIndent())
                }
            }
        }

//        withState()

        fallback { context ->
            cantProcess(context.message.chat.id)
        }
    }

    private fun help(context: Context) {
        telegram.sendMessage(context.message.chat.id, """
            Привет!
            
            Я умею отправлять нотификации о парах, когда они начинаются. Пока-что конфигурировать нотификации можно только через json/yaml. Пример конфига есть <a href="$exampleConfig">тут</a>.
            
            Если хочется конфигурировать кнопочками в боте, пинайте @darkkeks.

            Команды:
            <b>/export</b> [json/yaml] — получить текущий конфиг. Формат по умолчанию — yaml.
            <b>/import</b> — Импортировать конфиг. Формат всегда yaml, так как json — это подмножество yaml.
            <b>/start</b>, <b>/help</b> — это сообщение.
        """.trimIndent(), parseMode = ParseMode.HTML, disableWebPagePreview = true)
    }

    private fun export(chatId: Long, format: String?) {
        val (mapper, mimeType) = when (format) {
            "json" -> jsonWriteMapper to "application/json"
            "yaml", "yml" -> readMapper to "application/x-yaml"
            else -> null to null
        }

        if (mapper == null || mimeType == null) {
            telegram.sendMessage(chatId, """Я не умею экспортировать в формат `$format` 😟""",
                parseMode = ParseMode.MARKDOWN_V2)
            return
        }

        val userOptional = userRepository.findById(chatId)
        if (userOptional.isEmpty) {
            noConfig(chatId)
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
            """.trimIndent(), parseMode = ParseMode.MARKDOWN_V2)
            return
        }

        try {
            val document = MultipartBody.Part.createFormData("document", "config.$format",
                RequestBody.create(MediaType.parse(mimeType), content))
            telegram.sendDocument(document, chatId, caption = """
                Держи!
            """.trimIndent())
        } catch (e: Exception) {
            logger.error("Cant send exported config for user {}, config:" , chatId, content, e)
            telegram.sendMessage(chatId, """
                Странно, не получилось отправить файл с конфигом. 🤯
                Напиши пожалуйста @darkkeks. Твой репорт поможет найти проблему сразу, а не через inf дней.
            """.trimIndent(), parseMode = ParseMode.MARKDOWN_V2)
        }
    }

    fun noConfig(chatId: Long) {
        telegram.sendMessage(chatId, """
            Похоже у тебя еще нету конфига.
            Ты можешь создать его по примеру <a href="$exampleConfig">отсюда</a>, а потом импортировать с помощью <b>/import</>. 😉
        """.trimIndent(), parseMode = ParseMode.HTML, disableWebPagePreview = true)
    }

    fun cantProcess(chatId: Long) {
        telegram.sendMessage(chatId, """Не понимаю 😧""")
    }

    private fun importDocument(chatId: Long, document: Document) {
        val fileId = document.fileId
        val file = telegram.getFile(fileId)

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
                """.trimIndent(), parseMode = ParseMode.MARKDOWN_V2)
            return
        }

        val path = file.filePath
        if (path == null) {
            telegram.sendMessage(chatId, """
                    Почему-то у файла нету `file_path`. Напиши @darkkeks, или попробуй перезалить конфиг.
                """.trimIndent(), parseMode = ParseMode.MARKDOWN_V2)
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
            fromJson<UserSpec>(content, readMapper)
        } catch (e: Exception) {
            telegram.sendMessage(chatId, """
                Не получилось спарсить конфиг.
                Возможно тебе поможет ошибка: $e
            """.trimIndent())
            return
        }

        userConfigService.updateUserConfig(chatId, config)

        telegram.sendMessage(chatId, """
            Успешно сохранил новый конфиг! 👍
        """.trimIndent())
    }
}
