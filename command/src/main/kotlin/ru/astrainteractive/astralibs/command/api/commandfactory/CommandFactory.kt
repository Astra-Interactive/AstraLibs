package ru.astrainteractive.astralibs.command.api.commandfactory

import ru.astrainteractive.astralibs.command.api.command.Command
import ru.astrainteractive.astralibs.command.api.context.CommandContext
import ru.astrainteractive.astralibs.command.api.executor.CommandExecutor
import ru.astrainteractive.astralibs.command.api.parser.CommandParser
import ru.astrainteractive.astralibs.command.api.sideeffect.CommandSideEffect

/**
 * [CommandFactory] will create default [Command] instance. Use with delegates
 */
interface CommandFactory<CC : CommandContext> {
    @Suppress("LongParameterList")
    fun <R : Any, I : Any> create(
        alias: String,
        commandParser: CommandParser<R, CC>,
        commandExecutor: CommandExecutor<I> = CommandExecutor.NoOp(),
        commandSideEffect: CommandSideEffect<R, CC> = CommandSideEffect.NoOp(),
        mapper: Command.Mapper<R, I>
    ): Command<CC>
}
