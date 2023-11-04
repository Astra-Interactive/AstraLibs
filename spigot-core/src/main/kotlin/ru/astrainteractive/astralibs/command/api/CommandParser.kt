package ru.astrainteractive.astralibs.command.api

import org.bukkit.command.CommandSender

/**
 * CommandParser will parse your command and produce an output, with which you can continue execution
 */
interface CommandParser<R : Any> {
    val alias: String

    fun parse(
        args: Array<out String>,
        sender: CommandSender
    ): R

    class Default<R : Any>(
        override val alias: String,
        private val parse: (args: Array<out String>, sender: CommandSender) -> R
    ) : CommandParser<R> {

        override fun parse(args: Array<out String>, sender: CommandSender): R {
            return parse.invoke(args, sender)
        }
    }

    /**
     * Result handler will handle [CommandParser] output
     *
     * It can send a message to executor if parsing was not successful
     */
    fun interface ResultHandler<R : Any> {
        fun handle(commandSender: CommandSender, result: R)

        class NoOp<R : Any> : ResultHandler<R> {
            override fun handle(commandSender: CommandSender, result: R) = Unit
        }
    }
}
