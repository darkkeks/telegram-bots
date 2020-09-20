package ru.darkkeks.telegram.hseremind

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.core.createLogger
import java.time.ZonedDateTime

@Component
class SourceFetchService(val ruzApi: RuzApi) {
    private val logger = createLogger<SourceFetchService>()

    private val sources: MutableMap<Source, Int> = mutableMapOf()
    private val sourceToId: MutableMap<Source, Int> = mutableMapOf()

    private val sourceResults: MutableMap<Source, List<ScheduleItem>> = mutableMapOf()

    fun addSource(source: Source) {
        logger.info("Adding source {}", source)
        synchronized(sources) {
            val count = sources[source] ?: 0
            sources[source] = count + 1
        }
    }

    fun removeSource(source: Source) {
        logger.info("Removing source {}", source)
        synchronized(sources) {
            val count = sources[source] ?: 0
            if (count > 1) {
                sources[source] = count - 1
            } else {
                sources.remove(source)
                sourceResults.remove(source)
            }
        }
    }

    fun update() {
        sources.keys.forEach { source ->
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
                    sourceResults[source] = result
                }
            }
        }
    }

    private fun fetchSourceInfo(source: Source, id: Int): List<ScheduleItem>? {
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

    private fun fetchSourceId(source: Source): Int? {
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

    private fun sourceToTypeAndTerm(source: Source): Pair<String, String>? {
        return when (source) {
            is StudentSource -> "student" to source.student
            is GroupSource -> "group" to source.group
            else -> return null
        }
    }

    fun getSourceInfo(source: Source): List<ScheduleItem> {
        return sourceResults[source] ?: listOf()
    }
}
