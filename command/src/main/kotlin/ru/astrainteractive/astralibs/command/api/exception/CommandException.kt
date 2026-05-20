package ru.astrainteractive.astralibs.command.api.exception

/** Base class for all exceptions thrown during command parsing or execution. */
open class CommandException(message: String) : Throwable(message)
