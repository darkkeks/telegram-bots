package ru.darkkeks.telegram.core.serialize

import kotlin.reflect.KClass

class Registry<T : BufferSerializable> {
    private val entries: MutableMap<Byte, RegistryEntry<out T>> = mutableMapOf()
    private val idByClass: MutableMap<KClass<out T>, Byte> = mutableMapOf()

    fun <K : T> register(id: Byte, klass: KClass<K>, bufferDeserializer: BufferDeserializer<K>) {
        require(!entries.containsKey(id)) { "Item with id $id is already registered" }
        require(!idByClass.containsKey(klass)) { "Class $klass registered multiple times" }
        entries[id] = RegistryEntry(klass, bufferDeserializer)
        idByClass[klass] = id
    }

    fun getId(value: T): Byte? {
        return idByClass[value::class]
    }

    fun get(buffer: ButtonBuffer): T? {
        val id = buffer.peekByte()
        return entries[id]?.let { entry ->
            buffer.popByte()
            entry.deserializer.read(buffer)
        }
    }

    class RegistryEntry<K : BufferSerializable>(
            val klass: KClass<K>,
            val deserializer: BufferDeserializer<K>
    )
}
