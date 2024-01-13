package ru.astrainteractive.astralibs.command.types

fun <T : Any> ArgumentType<T?>.withDefault(default: T): ArgumentType<T> {
    return ArgumentType { transform(it) ?: default }
}
