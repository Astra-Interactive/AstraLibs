package ru.astrainteractive.astralibs

/**
 * [Factory] is a fun interface which can build data for your classes
 *
 * It's look similar to [Provider] but it's more convenient to use different naming for this two
 */
fun interface Factory<out T> {
    fun build(): T
}