package ru.astrainteractive.astralibs.command.type

object ArgumentTypeExt {
    fun <T : Any> ArgumentType<T?>.withDefault(default: T): ArgumentType<T> {
        return ArgumentType { transform(it) ?: default }
    }
}
