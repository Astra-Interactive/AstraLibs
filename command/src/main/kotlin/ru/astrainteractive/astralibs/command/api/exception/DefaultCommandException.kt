package ru.astrainteractive.astralibs.command.api.exception

import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentType
import ru.astrainteractive.astralibs.permission.Permission

sealed class DefaultCommandException(message: String) : CommandException(message) {
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
}
