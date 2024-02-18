package ru.astrainteractive.astralibs.command.api.sideeffect

import ru.astrainteractive.astralibs.command.api.context.CommandContext
import ru.astrainteractive.astralibs.command.api.parser.CommandParser

/**
 * Result handler will handle [CommandParser] output
 *
 * It can send a message to executor if parsing was not successful
 */
fun interface CommandSideEffect<R : Any, CC : CommandContext> {
    /**
     * Handle your parsed result. Send message etc
     */
    fun handle(commandContext: CC, result: R)

    class NoOp<R : Any, CC : CommandContext> : CommandSideEffect<R, CC> {
        override fun handle(commandContext: CC, result: R) = Unit
    }
}
