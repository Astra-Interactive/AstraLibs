package ru.astrainteractive.astralibs.command.api

import ru.astrainteractive.astralibs.command.registerCommand

object DefaultCommandFactory : CommandFactory {
    override fun <R : Any, I : Any> create(
        alias: String,
        commandParser: CommandParser<R>,
        commandExecutor: CommandExecutor<I>,
        resultHandler: Command.ResultHandler<R>,
        mapper: Command.Mapper<R, I>
    ) = Command<R, I> { plugin ->
        plugin.registerCommand(alias) {
            commandParser.parse(args, sender)
                .also { resultHandler.handle(sender, it) }
                .let(mapper::toInput)
                ?.let(commandExecutor::execute)
        }
    }
}
