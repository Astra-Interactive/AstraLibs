package ru.astrainteractive.astralibs.command.model

/**
 * Thrown when a command argument value is incompatible with the expected
 * [ru.astrainteractive.astralibs.command.argumenttype.api.ArgumentConverter] type.
 */
public class BadArgumentException(
    wrongArgument: String?,
    type: Any
) : CommandException("Incompatible type $type for argument $wrongArgument")
