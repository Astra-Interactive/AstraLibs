package ru.astrainteractive.astralibs.command.api.parser

import ru.astrainteractive.astralibs.command.api.context.CommandContext

/**
 * CommandParser will parse your command and produce an output, with which you can continue execution
 */
fun interface CommandParser<R : Any, CC : CommandContext> {
    /**
     * Parse arguments into Result [R]
     */
    fun parse(commandContext: CC): R
}
