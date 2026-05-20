package ru.astrainteractive.astralibs.util

/** Filters to strings containing [entry]; returns `this` when [entry] is null or empty. */
fun List<String>.withEntry(entry: String?, ignoreCase: Boolean = true): List<String> {
    if (entry.isNullOrEmpty()) return this
    return filter { item -> item.contains(other = entry, ignoreCase = ignoreCase) }
}
