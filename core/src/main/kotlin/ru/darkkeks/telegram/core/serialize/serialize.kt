package ru.darkkeks.telegram.core.serialize

interface BufferSerializable {
    fun write(buffer: ButtonBuffer) {}
}

interface BufferDeserializer<T : BufferSerializable> {
    fun read(buffer: ButtonBuffer): T
}

fun Byte.toUnsigned() = if (this >= 0) this.toInt() else 256 + this.toInt()

fun ButtonBuffer.peekInt(): Int {
    val (a, b, c, d) = peekBytes(4)
    return (a.toUnsigned() shl 24) or
            (b.toUnsigned() shl 16) or
            (c.toUnsigned() shl 8) or
            d.toUnsigned()
}

fun ButtonBuffer.popInt(): Int {
    val result = peekInt()
    repeat(4) { popByte() }
    return result
}

fun ButtonBuffer.pushInt(value: Int) {
    val shifts = (0..3).reversed().map { it * 8 }
    shifts.forEach {
        pushByte(((value ushr it) and 0xFF).toByte())
    }
}


fun ButtonBuffer.peekBoolean(): Boolean = peekByte() != 0.toByte()
fun ButtonBuffer.popBoolean(): Boolean = popByte() != 0.toByte()
fun ButtonBuffer.pushBoolean(value: Boolean) = pushByte(if(value) 1 else 0)


fun ButtonBuffer.peekString(): String {
    val length = peekByte().toInt()
    val result = peekBytes(length)
    return String(result.toByteArray())
}

fun ButtonBuffer.popString(): String {
    val length = popByte().toInt()
    val result = popBytes(length)
    return String(result.toByteArray())
}

fun ButtonBuffer.pushString(value: String) {
    val bytes = value.toByteArray()
    pushByte(bytes.size.toByte())
    bytes.forEach { pushByte(it) }
}
