package ru.astrainteractive.astralibs.command.api

import org.bukkit.command.CommandSender

/**
 * CommandParser will parse your command and produce an output, with which you can continue execution
 */
fun interface CommandParser<R : Any> {
    /**
     * Parse arguments into Result [R]
     */
    fun parse(
        args: Array<out String>,
        sender: CommandSender
    ): R
}
