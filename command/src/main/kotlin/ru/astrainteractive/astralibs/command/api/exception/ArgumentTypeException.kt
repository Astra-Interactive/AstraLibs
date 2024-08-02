package ru.astrainteractive.astralibs.command.api.exception

class ArgumentTypeException(
    key: String,
    value: String
) : CommandException("Argument type $key could not parse $value")
