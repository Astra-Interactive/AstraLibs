package ru.astrainteractive.astralibs.command.types

fun interface ArgumentType<T> {
    fun transform(value: String?): T
}
