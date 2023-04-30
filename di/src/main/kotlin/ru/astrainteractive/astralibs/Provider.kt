package ru.astrainteractive.astralibs

/**
 * [Provider] is a fun interface which can provider some data for your dependency
 *
 * It's look similar to [Factory] but it's more convenient to use different naming for this two
 */
fun interface Provider<out T> {
    fun provide(): T
}