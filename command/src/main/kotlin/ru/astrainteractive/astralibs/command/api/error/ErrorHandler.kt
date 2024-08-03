package ru.astrainteractive.astralibs.command.api.error

import ru.astrainteractive.astralibs.command.api.context.CommandContext

fun interface ErrorHandler<CC : CommandContext> {
    fun handle(commandContext: CC, throwable: Throwable)
}
