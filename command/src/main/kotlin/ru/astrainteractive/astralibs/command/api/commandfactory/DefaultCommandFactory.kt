package ru.astrainteractive.astralibs.command.api.commandfactory

import ru.astrainteractive.astralibs.command.api.command.Command
import ru.astrainteractive.astralibs.command.api.context.CommandContext
import ru.astrainteractive.astralibs.command.api.executor.CommandExecutor
import ru.astrainteractive.astralibs.command.api.parser.CommandParser
import ru.astrainteractive.astralibs.command.api.sideeffect.CommandSideEffect

class DefaultCommandFactory<CC : CommandContext> : CommandFactory<CC> {
    override fun <R : Any, I : Any> create(
        alias: String,
        commandParser: CommandParser<R, CC>,
        commandExecutor: CommandExecutor<I>,
        commandSideEffect: CommandSideEffect<R, CC>,
        mapper: Command.Mapper<R, I>
    ) = object : Command<CC> {
        override val alias: String = alias

        override fun dispatch(commandContext: CC) {
            commandParser.parse(commandContext)
                .also { commandSideEffect.handle(commandContext, it) }
                .let(mapper::toInput)
                ?.let(commandExecutor::execute)
        }
    }
}
