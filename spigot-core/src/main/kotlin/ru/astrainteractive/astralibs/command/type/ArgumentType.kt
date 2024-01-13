package ru.astrainteractive.astralibs.command.type

fun interface ArgumentType<T> {
    fun transform(value: String?): T
}
