package ru.darkkeks.telegram.trackyoursheet

import kotlin.math.max
import kotlin.math.min


fun nullableMin(a: Int?, b: Int?): Int? {
    a ?: return null
    b ?: return null
    return min(a, b)
}

fun nullableMax(a: Int?, b: Int?): Int? {
    a ?: return null
    b ?: return null
    return max(a, b)
}
