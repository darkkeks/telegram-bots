package ru.darkkeks.telegram.trackyoursheet.sheets

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.google.api.services.sheets.v4.model.GridData
import java.time.Instant

class RangeData {

    val start: Cell
    val data: List<List<String>>
    val job: Id<Range>
    val time: Instant
    val _id: Id<RangeData>

    @JsonSerialize(keyUsing = Cell.CellKeySerializer::class)
    @JsonDeserialize(keyUsing = Cell.CellKeyDeserializer::class)
    val notes: Map<Cell, String>

    @JsonCreator
    constructor(start: Cell,
                data: List<List<String>>,
                notes: Map<Cell, String>,
                job: Id<Range>,
                time: Instant = Instant.now(),
                _id: Id<RangeData> = newId()) {
        this.start = start
        this.data = data
        this.notes = notes
        this.job = job
        this.time = time
        this._id = _id
    }

    constructor(grid: GridData,
                job: Id<Range>,
                time: Instant = Instant.now(),
                _id: Id<RangeData> = newId()) {
        start = Cell(grid.startRow ?: 0, grid.startColumn ?: 0)
        this.job = job
        this.time = time
        this._id = _id

        val mutableNotes = mutableMapOf<Cell, String>()
        data = List(grid.rowData.size) { i ->
            val row = grid.rowData[i].getValues()
            List(row.size) { j ->
                val cell = row[j]
                if (cell.note != null) {
                    mutableNotes[Cell(i, j)] = cell.note
                }
                cell.formattedValue ?: ""
            }
        }
        notes = mutableNotes
    }

    val dimensions get() = when {
        data.isNotEmpty() -> data.size to data[0].size
        else -> 0 to 0
    }
}
