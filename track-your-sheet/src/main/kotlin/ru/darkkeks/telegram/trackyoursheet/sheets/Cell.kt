package ru.darkkeks.telegram.trackyoursheet.sheets

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.SerializerProvider


data class Cell(val row: Int?, val column: Int?) {

    operator fun plus(shift: Pair<Int?, Int?>) = Cell(
            (row ?: 0) + (shift.first ?: 0),
            (column ?: 0) + (shift.second ?: 0)
    )

    operator fun plus(shift: Cell) = this + (shift.row to shift.column)

    operator fun plus(shift: Int) = this + (shift to shift)

    override fun toString() = "${indexToSheetString(column)}${row}"

    companion object {
        val ROW_PATTERN = """[1-9]\d*""".toRegex()

        val COLUMN_PATTERN = """[A-Z]+""".toRegex()

        val CELL_PATTERN = """($COLUMN_PATTERN)($ROW_PATTERN)""".toRegex()

        fun fromString(value: String): Cell? {
            return when {
                value matches CELL_PATTERN -> {
                    val (col, row) = CELL_PATTERN.find(value)!!.groupValues.drop(1)
                    Cell(row.toInt(), sheetStringToIndex(col))
                }
                value matches ROW_PATTERN -> {
                    Cell(value.toInt(), null)
                }
                value matches COLUMN_PATTERN -> {
                    Cell(null, sheetStringToIndex(value))
                }
                else -> null
            }
        }

        private fun sheetStringToIndex(value: String?): Int {
            value ?: return -1
            var result = 0
            for (char in value) {
                result *= 26
                result += char - 'A' + 1
            }
            return result
        }

        private fun indexToSheetString(index: Int?): String {
            index ?: return "?"
            var value = index
            var result = ""
            while (value > 0) {
                val digit = value % 26
                if (digit == 0) {
                    result += 'Z'
                    value -= 26
                } else {
                    result += 'A' + digit - 1
                }
                value /= 26
            }
            return result.reversed()
        }
    }

    class CellKeySerializer : JsonSerializer<Cell>() {
        override fun serialize(value: Cell, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeFieldName("${value.row},${value.column}")
        }
    }

    class CellKeyDeserializer : KeyDeserializer() {
        override fun deserializeKey(key: String, ctxt: DeserializationContext): Any {
            val (r, c) = key.split(",")
            return Cell(r.toInt(), c.toInt())
        }
    }
}
