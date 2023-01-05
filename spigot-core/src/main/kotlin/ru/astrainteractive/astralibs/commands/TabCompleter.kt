package ru.astrainteractive.astralibs.commands

import org.bukkit.command.CommandSender

class TabCompleter(val alias: String, val sender: CommandSender, val args: Array<out String>) {

    fun <T> argument(
        index: Int,
        parser: (String?) -> T?,
        onError: (String?) -> Unit = {},
        onResult: (Command.Argument<T>) -> Unit
    ) {
        val argumentRawValue = args.getOrNull(index)
        val argumentValue = parser.invoke(argumentRawValue)
        argumentValue?.let { onResult(Command.Argument(it, argumentRawValue)) }
            ?: onError(argumentRawValue)
    }
}


