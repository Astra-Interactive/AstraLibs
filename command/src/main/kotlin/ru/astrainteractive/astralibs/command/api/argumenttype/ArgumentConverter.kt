package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.CommandException
import kotlin.jvm.Throws

/**
 * Converts a raw command argument string into a typed value [T].
 *
 * @throws CommandException when the input cannot be converted.
 */
fun interface ArgumentConverter<T : Any> {
    @Throws(CommandException::class)
    fun transform(argument: String): T
}
