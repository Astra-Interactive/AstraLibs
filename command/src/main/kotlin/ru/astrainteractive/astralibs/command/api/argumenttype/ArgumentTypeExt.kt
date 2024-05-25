package ru.astrainteractive.astralibs.command.api.argumenttype

object ArgumentTypeExt {
    fun <T : Any> ArgumentType<T?>.withDefault(default: T): ArgumentType<T> {
        return ArgumentType { transform(it) ?: default }
    }
}
