package ru.astrainteractive.astralibs.command.api.exception

import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter
import ru.astrainteractive.astralibs.permission.Permission
import ru.astrainteractive.astralibs.string.StringDesc

class StringDescCommandException(
    val stringDesc: StringDesc
) : CommandException("Specific StringDesc exception: $stringDesc")

class BadArgumentException(
    wrongArgument: String?,
    type: ArgumentConverter<*>
) : CommandException("Incompatible type $type for argument $wrongArgument")

class ArgumentConverterException(
    clazz: Class<out ArgumentConverter<*>>,
    value: String
) : CommandException("Argument type $clazz could not parse $value")

class NoPermissionException(
    permission: Permission
) : CommandException("No permission: $permission")

class NoPlayerException(
    name: String
) : CommandException("Player $name not found")

class NotPlayerExecutorException : CommandException("Executor should be player")

class NoPotionEffectTypeException(
    name: String
) : CommandException("PotionEffectType $name not found")
