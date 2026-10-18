package ru.astrainteractive.astralibs.command.model

/**
 * Thrown by an [ru.astrainteractive.astralibs.command.argumenttype.api.ArgumentConverter] when it cannot parse
 * [value] into its target type.
 */
public class ArgumentConverterException(
    clazz: Class<*>,
    value: String
) : CommandException("Argument type $clazz could not parse $value")
