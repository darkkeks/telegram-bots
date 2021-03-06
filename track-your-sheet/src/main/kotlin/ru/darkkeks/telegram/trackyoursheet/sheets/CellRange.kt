package ru.darkkeks.telegram.trackyoursheet.sheets

import ru.darkkeks.telegram.trackyoursheet.nullableMax
import ru.darkkeks.telegram.trackyoursheet.nullableMin


class CellRange(from: Cell, to: Cell) {
    val from = Cell(nullableMin(from.row, to.row), nullableMin(from.column, to.column))
    val to = Cell(nullableMax(from.row, to.row), nullableMax(from.column, to.column))

    override fun toString() = "$from:$to"

    companion object {
        private val RANGE_PATTERN = listOf(
                "${Cell.CELL_PATTERN}:${Cell.CELL_PATTERN}",
                "${Cell.ROW_PATTERN}:${Cell.ROW_PATTERN}",
                "${Cell.COLUMN_PATTERN}:${Cell.COLUMN_PATTERN}",
                "${Cell.CELL_PATTERN}",
                "${Cell.ROW_PATTERN}",
                "${Cell.COLUMN_PATTERN}"
        ).joinToString("|").toRegex()

        fun isRange(value: String) = value matches RANGE_PATTERN

        fun fromString(value: String): CellRange {
            require(isRange(value))

            return if (value.contains(":")) {
                val (left, right) = value.split(":")
                val from = Cell.fromString(left)
                val to = Cell.fromString(right)

                require(from != null && to != null)
                CellRange(from, to)
            } else {
                val cell = Cell.fromString(value)
                require(cell != null)
                CellRange(cell, cell)
            }
        }
    }
}
