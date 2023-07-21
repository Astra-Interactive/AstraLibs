package ru.astrainteractive.astralibs.commands.types

fun interface ArgumentType<T> {
    fun transform(value: String): T
}
