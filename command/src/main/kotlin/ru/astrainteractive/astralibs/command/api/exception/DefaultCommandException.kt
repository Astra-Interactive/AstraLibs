package ru.astrainteractive.astralibs.command.api.exception

import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentType
import ru.astrainteractive.astralibs.permission.Permission
import ru.astrainteractive.astralibs.string.StringDesc

sealed class DefaultCommandException(message: String) : CommandException(message)

class BadArgumentException(
    wrongArgument: String?,
    type: ArgumentType<*>
) : DefaultCommandException("Incompatible type $type for argument $wrongArgument")

class ArgumentTypeException(
    key: String,
    value: String
) : DefaultCommandException("Argument type $key could not parse $value")

class NoPermissionException(
    permission: Permission
) : DefaultCommandException("No permission: $permission")

class NoPlayerException(
    name: String
) : DefaultCommandException("Player $name not found")

class NoPotionEffectTypeException(
    name: String
) : DefaultCommandException("PotionEffectType $name not found")

class StringDescException(val stringDesc: StringDesc) : CommandException("Specific StringDesc exception: $stringDesc")
