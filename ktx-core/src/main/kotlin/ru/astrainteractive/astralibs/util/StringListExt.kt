package ru.astrainteractive.astralibs.util

object StringListExt {
    /**
     * If you have list with entries {"entry","ementry","emementry"} and entry="me",
     * you'll have returned list {"ementry","emementry"}.
     *
     * Very useful for TabCompleter
     *
     */
    fun List<String>.withEntry(entry: String?, ignoreCase: Boolean = true): List<String> {
        if (entry == null) return this
        return filter { item -> item.contains(other = entry, ignoreCase = ignoreCase) }
    }
}
