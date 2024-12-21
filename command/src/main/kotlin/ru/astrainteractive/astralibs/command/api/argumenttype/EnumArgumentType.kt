package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentTypeException
import kotlin.enums.EnumEntries

/**
 * Enum argument is used to define enumeration of arguments
 * @see EnumArgumentType
 */
interface EnumArgument {
    /**
     * This value will be checked for command argument
     */
    val value: String
}

/**
 * When your command is like /amarket <open|expired|players> - you can use enums
 */
class EnumArgumentType<T>(
    private val entries: EnumEntries<T>
) : PrimitiveArgumentType<Enum<T>> where T : Enum<T>, T : EnumArgument {
    override val key: String = "ENUM"

    override fun transform(value: String): Enum<T> {
        return entries.firstOrNull { entry -> entry.value.equals(value, ignoreCase = true) }
            ?.ordinal
            ?.let(entries::getOrNull)
            ?: throw ArgumentTypeException(key, value)
    }
}
