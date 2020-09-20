package ru.darkkeks.telegram.core

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import kotlin.reflect.KClass


class PropertyPresentPolymorphicDeserializer<T>(vc: Class<T>) : StdDeserializer<T>(vc) {

    private val propertyNameToType: Map<String, KClass<*>>

    init {
        propertyNameToType = vc.getAnnotation(JsonSubTypes::class.java).value
                .distinctBy { it.name }
                .map { type -> type.name to type.value }
                .toMap(LinkedHashMap())
    }

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): T {
        val objectMapper: ObjectMapper = p.codec as ObjectMapper
        val obj: ObjectNode = objectMapper.readTree(p)
        for (propertyName in propertyNameToType.keys) {
            if (obj.has(propertyName)) {
                val klass = propertyNameToType[propertyName] ?: throw IllegalArgumentException()
                return objectMapper.treeToValue(obj, klass.java) as T
            }
        }
        throw IllegalArgumentException("could not infer to which class to deserialize $obj")
    }
}
