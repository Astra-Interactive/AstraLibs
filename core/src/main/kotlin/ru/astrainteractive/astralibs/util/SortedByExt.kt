package ru.astrainteractive.astralibs.util

import kotlin.collections.sortedBy
import kotlin.collections.sortedByDescending

inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(
    isAscending: Boolean,
    crossinline selector: (T) -> R?
): List<T> {
    return if (isAscending) {
        sortedBy(selector)
    } else {
        sortedByDescending(selector)
    }
}
