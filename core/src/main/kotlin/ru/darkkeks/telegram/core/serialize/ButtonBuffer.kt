package ru.darkkeks.telegram.core.serialize

class ButtonBuffer(value: String = "") {

    private val bytes: MutableList<Byte> = fromString(value).reversed().toMutableList()

    val size get() = bytes.size

    fun pushByte(byte: Byte) {
        require(bytes.size < MAX_SIZE)
        bytes.add(byte)
    }

    fun popByte(): Byte {
        require(bytes.size > 0)
        return bytes.removeAt(bytes.lastIndex)
    }

    fun peekByte(): Byte {
        require(bytes.size > 0)
        return bytes.last()
    }

    fun popBytes(count: Int): List<Byte> {
        require(count <= bytes.size)
        return buildList {
            repeat(count) {
                add(popByte())
            }
        }
    }

    fun peekBytes(count: Int): List<Byte> {
        require(count <= bytes.size)
        return bytes.subList(bytes.size - count, bytes.size).reversed()
    }

    fun serialize(): String {
        return toString(bytes)
    }

    override fun toString(): String {
        return bytes.joinToString(prefix = "[", postfix = "]")
    }

    companion object {
        private const val DATA_MAX_SIZE = 64
        private const val MAX_SIZE = DATA_MAX_SIZE * 7 / 8

        private fun fromString(string: String) = buildList {
            var current: Long = 0
            var count = 0
            string.forEach { char ->
                val code = char.toLong()
                current = current or (code shl count)
                count += 7

                while (count >= 8) {
                    val byte = (current and 0xFF).toByte()
                    add(byte)
                    current = current shr 8
                    count -= 8
                }
            }

            require(current == 0L) {
                "Leftover bits"
            }
        }

        private fun toString(bytes: List<Byte>) = buildString {
            var current: Long = 0
            var count = 0

            fun addAll(minBitCount: Int = 7) {
                while (count >= minBitCount) {
                    append((current and 0x7F).toChar())
                    current = current shr 7
                    count -= 7
                }
            }

            bytes.forEach { byte ->
                current = current or (byte.toUnsigned().toLong() shl count)
                count += 8

                addAll(7)
            }

            addAll(1)
        }
    }
}
