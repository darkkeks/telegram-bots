package ru.darkkeks.telegram.hseremind

import ru.darkkeks.telegram.core.createLogger
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

interface SourceFetchService {
    fun addSource(source: Source)
    fun removeSource(source: Source)
    fun update()
}

abstract class AbstractSourceFetchService<T : Source, R : Any> : SourceFetchService {
    private val logger = createLogger(AbstractSourceFetchService::class)

    abstract val sourceType: KClass<T>

    private val sources: MutableMap<T, Int> = mutableMapOf()
    private val sourceResults: MutableMap<T, R> = mutableMapOf()

    override fun addSource(source: Source) {
        val typed = sourceType.safeCast(source) ?: return
        addTypedSource(typed)
    }

    override fun removeSource(source: Source) {
        val typed = sourceType.safeCast(source) ?: return
        removeTypedSource(typed)
    }

    private fun addTypedSource(source: T) {
        logger.info("Adding source {}", source)
        synchronized(sources) {
            val count = sources[source] ?: 0
            sources[source] = count + 1
        }
    }

    private fun removeTypedSource(source: T) {
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

    fun getSources() = sources.keys

    fun putSourceInfo(source: T, result: R) {
        sourceResults[source] = result
    }

    fun getSourceInfo(source: T): R? {
        return sourceResults[source]
    }

    open fun onSourceAdded() {}

    open fun onSourceRemoved() {}
}
