package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentTypeException
import kotlin.jvm.Throws

interface ArgumentType<T : Any> {
    val key: String

    @Throws(ArgumentTypeException::class)
    fun transform(value: String): T
}
