package ru.darkkeks.telegram.hseremind.ruz

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.createLogger
import ru.darkkeks.telegram.hseremind.*
import java.time.ZonedDateTime
import kotlin.reflect.KClass

@Component
class RuzSourceFetchService(val ruzApi: RuzApi) : AbstractSourceFetchService<RuzSource, List<ScheduleItem>>() {
    private val logger = createLogger<SourceFetchService>()

    override val sourceType: KClass<RuzSource> = RuzSource::class

    private val sourceToId: MutableMap<RuzSource, Int> = mutableMapOf()

    override fun update() {
        getSources().forEach { source ->
            var id = sourceToId[source]
            if (id == null) {
                id = fetchSourceId(source)
                if (id != null) {
                    sourceToId[source] = id
                }
            }

            if (id != null) {
                val result = fetchSourceInfo(source, id)
                if (result != null) {
                    putSourceInfo(source, result)
                }
            }
        }
    }

    private fun fetchSourceInfo(source: RuzSource, id: Int): List<ScheduleItem>? {
        val (type, _) = sourceToTypeAndTerm(source) ?: return null

        val today = ZonedDateTime.now(RuzUtils.moscowZoneId)
        val formattedDate = today.format(RuzUtils.dateFormatter)

        logger.info("Fetching schedule for source {} with id {} and date {}", source, id, formattedDate)

        val request = ruzApi.schedule(type, id,
                start = today.format(RuzUtils.dateFormatter),
                finish = today.format(RuzUtils.dateFormatter))

        val response = try {
            request.execute()
        } catch (e: Exception) {
            logger.warn("Failed to fetch source schedule", e)
            return null
        }

        return if (response.isSuccessful) {
            val result = response.body()
            logger.info("Successfully fetched {} schedule items for date {}", result?.size ?: 0, formattedDate)
            result
        } else {
            logger.error("Failed to get schedule: ", response.errorBody())
            null
        }
    }

    private fun fetchSourceId(source: RuzSource): Int? {
        val (type, term) = sourceToTypeAndTerm(source) ?: return null

        val response = try {
            ruzApi.search(term, type).execute()
        } catch (e: Exception) {
            logger.warn("Failed to fetch source id", e)
            return null
        }

        return if (response.isSuccessful) {
            response.body()?.firstOrNull()?.id
        } else {
            logger.warn("Failed to fetch source id", response.errorBody())
            null
        }
    }

    private fun sourceToTypeAndTerm(source: RuzSource): Pair<String, String>? {
        return when (source) {
            is StudentSource -> "student" to source.student
            is GroupSource -> "group" to source.group
            else -> return null
        }
    }
}
