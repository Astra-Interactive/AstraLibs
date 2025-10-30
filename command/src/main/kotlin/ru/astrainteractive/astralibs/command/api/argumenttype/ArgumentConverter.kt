package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException
import kotlin.jvm.Throws

fun interface ArgumentConverter<T : Any> {
    @Throws(ArgumentConverterException::class)
    fun transform(argument: String): T
}
