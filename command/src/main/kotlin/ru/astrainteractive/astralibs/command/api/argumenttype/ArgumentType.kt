package ru.astrainteractive.astralibs.command.api.argumenttype

fun interface ArgumentType<T> {
    fun transform(value: String?): T
}
