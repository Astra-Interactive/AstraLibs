package ru.astrainteractive.astralibs.command.api

/**
 * [CommandFactory] will create default [Command] instance. Use with delegates
 */
interface CommandFactory {
    @Suppress("LongParameterList")
    fun <R : Any, I : Any> create(
        alias: String,
        commandParser: CommandParser<R>,
        commandExecutor: CommandExecutor<I> = CommandExecutor.NoOp(),
        resultHandler: Command.ResultHandler<R> = Command.ResultHandler.NoOp(),
        mapper: Command.Mapper<R, I>
    ): Command<R, I>
}
