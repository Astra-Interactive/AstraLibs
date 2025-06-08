package ru.astrainteractive.astralibs.util

/**
 * Filters the list of strings to include only those that contain the specified entry.
 *
 * This function is useful for scenarios like TabCompleter, where you need to find entries
 * in a list that contain a specific substring. The comparison can be case-sensitive or case-insensitive.
 *
 * Example usage:
 * ```kotlin
 * val list = listOf("entry", "ementry", "emementry")
 * val filteredList = list.withEntry("me")
 * // Returns: ["ementry", "emementry"]
 * ```
 *
 * @param entry The substring to search for within each string in the list. If `null`, the original list is returned.
 * @param ignoreCase If `true`, the search is case-insensitive; otherwise, it is case-sensitive. Defaults to `true`.
 * @return A new list containing only the strings that include the specified entry.
 */
fun List<String>.withEntry(entry: String?, ignoreCase: Boolean = true): List<String> {
    if (entry.isNullOrEmpty()) return this
    return filter { item -> item.contains(other = entry, ignoreCase = ignoreCase) }
}
