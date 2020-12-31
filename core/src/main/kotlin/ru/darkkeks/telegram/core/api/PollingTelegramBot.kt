package ru.darkkeks.telegram.core.api

import ru.darkkeks.telegram.core.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class PollingTelegramBot(private val client: Telegram,
                         private val executor: ScheduledExecutorService,
                         private val handler: UpdateHandler,
                         private var offset: Int? = null) {

    private val logger = createLogger<PollingTelegramBot>()

    private var task: ScheduledFuture<*>? = null

    fun startLongPolling() {
        task = executor.scheduleAtFixedRate({
            try {
                fetchUpdates()
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            } catch (e: Exception) {
                logger.error("Could not fetch updates", e)
            }
        }, 0, 1, TimeUnit.MILLISECONDS)
    }

    fun stop() {
        task?.cancel(true)
    }

    private fun fetchUpdates() {
        val limit = 100
        val timeout = 5

        val updates = try {
            client.getUpdates(offset, limit, timeout)
        } catch (e: TelegramClientException) {
            logger.error("Failed to fetch updates (offset = {}, limit = {}, timeout = {})", offset, limit, timeout, e)
            return
        }

        updates.forEach { update ->
            try {
                handler.handle(update)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                return
            } catch (e: Exception) {
                logger.warn("Could not handle update $update, dropping it", e)
            }

            offset = update.updateId + 1
        }
    }
}
