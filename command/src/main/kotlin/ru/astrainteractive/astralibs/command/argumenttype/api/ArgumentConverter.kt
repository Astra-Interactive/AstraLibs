package ru.astrainteractive.astralibs.command.argumenttype.api

import ru.astrainteractive.astralibs.command.model.ArgumentConverterException
import kotlin.jvm.Throws

/**
 * Converts a raw command argument string into a typed value [T].
 *
 * @throws ArgumentConverterException when the input cannot be converted.
 */
public fun interface ArgumentConverter<T : Any> {
    @Throws(ArgumentConverterException::class)
    public fun transform(argument: String): T
}
