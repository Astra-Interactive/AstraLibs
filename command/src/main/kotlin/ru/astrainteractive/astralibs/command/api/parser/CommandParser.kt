package ru.astrainteractive.astralibs.command.api.parser

import ru.astrainteractive.astralibs.command.api.context.CommandContext
import ru.astrainteractive.astralibs.command.api.exception.CommandException
import kotlin.jvm.Throws

/**
 * CommandParser will parse your command and produce an output, with which you can continue execution
 */
fun interface CommandParser<R : Any, CC : CommandContext> {
    /**
     * Parse arguments into Result [R]
     */
    @Throws(CommandException::class)
    fun parse(ctx: CC): R
}
