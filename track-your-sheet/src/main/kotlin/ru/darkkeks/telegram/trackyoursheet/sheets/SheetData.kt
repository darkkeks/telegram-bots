package ru.darkkeks.telegram.trackyoursheet.sheets

import com.fasterxml.jackson.annotation.JsonIgnore
import java.net.URI
import java.net.URISyntaxException

data class SheetData(val id: String, val sheetId: Int, val sheetName: String = "") {

    @JsonIgnore
    fun getUrl() = "https://docs.google.com/spreadsheets/d/${id}"

    @JsonIgnore
    fun getSheetUrl() = getUrl() + "#gid=${sheetId}"

    fun urlTo(cell: Cell) = getSheetUrl() + "&range=$cell"
    fun urlTo(value: String) = getSheetUrl() + "&range=$value"
    fun urlTo(range: CellRange) = getSheetUrl() + "&range=$range"

    companion object {
        private val DOMAIN_PATTERN = """(www\.)?docs\.google\.com""".toRegex()
        private val PATH_PATTERN = """/spreadsheets/d/([a-zA-Z\d-_]+)/?.*?""".toRegex()

        data class SheetUrlMatchResult(
                val id: String,
                val arguments: Map<String, String> = emptyMap()
        )

        /**
         * Extracts spreadsheet id and url arguments
         * @return Pair of spreadsheet id and map of arguments, or null if couldn't match
         */
        fun fromUrl(string: String): SheetUrlMatchResult? {
            var processed = string
            if (!string.startsWith("http")) {
                processed = "https://$processed"
            }

            val url = try {
                URI(processed)
            } catch (e: URISyntaxException) {
                return null
            }

            if (!DOMAIN_PATTERN.matches(url.host)) return null
            val path = PATH_PATTERN.matchEntire(url.path) ?: return null
            val id = path.groupValues[1]

            val hash: String? = url.fragment
            val parts = if (hash == null || hash.isEmpty()) listOf() else hash.split("&")
            val args = parts.map {
                val x = it.split('=', limit = 2)
                if (x.size == 2) x[0] to x[1] else x[0] to ""
            }.toMap()

            return SheetUrlMatchResult(id, args)
        }
    }
}
