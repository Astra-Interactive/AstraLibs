package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException
import ru.astrainteractive.astralibs.command.api.exception.CommandException
import ru.astrainteractive.astralibs.command.api.exception.NoPlayerException
import kotlin.jvm.Throws

/**
 * Converts a raw command argument string into a typed value [T].
 *
 * @throws CommandException when the input cannot be converted: usually [ArgumentConverterException], or a more
 * specific one such as [NoPlayerException] when the value names a player the server does not know.
 */
fun interface ArgumentConverter<T : Any> {
    @Throws(CommandException::class)
    fun transform(argument: String): T
}
