package ru.astrainteractive.astralibs.command.api.exception

import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter
import ru.astrainteractive.astralibs.server.permission.Permission
import ru.astrainteractive.astralibs.string.StringDesc

/** Thrown when a command execution fails with a localised [stringDesc] message. */
class StringDescCommandException(
    val stringDesc: StringDesc
) : CommandException("Specific StringDesc exception: $stringDesc")

/** Thrown when a command argument value is incompatible with the expected [ArgumentConverter] type. */
class BadArgumentException(
    wrongArgument: String?,
    type: ArgumentConverter<*>
) : CommandException("Incompatible type $type for argument $wrongArgument")

/** Thrown by an [ArgumentConverter] when it cannot parse [value] into its target type. */
class ArgumentConverterException(
    clazz: Class<out ArgumentConverter<*>>,
    value: String
) : CommandException("Argument type $clazz could not parse $value")

/** Thrown when the command executor does not hold a required [Permission]. */
class NoPermissionException(
    permission: Permission
) : CommandException("No permission: $permission")

/** Thrown when a player lookup yields no result. */
class NoPlayerException(
    name: String
) : CommandException("Player $name not found")

/** Thrown when a command that requires a player executor is run by a non-player sender. */
class NotPlayerExecutorException : CommandException("Executor should be player")

/** Thrown when a potion effect type lookup yields no result. */
class NoPotionEffectTypeException(
    name: String
) : CommandException("PotionEffectType $name not found")
