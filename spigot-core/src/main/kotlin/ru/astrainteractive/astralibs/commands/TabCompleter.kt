package ru.astrainteractive.astralibs.commands

import org.bukkit.command.CommandSender

class TabCompleter(val alias: String, val sender: CommandSender, val args: Array<out String>) {
        fun <T> argument(
        index: Int,
        converter: (String?) -> T?,
    ): Argument<T> {
        val argumentRawValue = args.getOrNull(index)
        val argumentValue = converter.invoke(argumentRawValue)

        return argumentValue?.let { Argument.Success(it, argumentRawValue) } ?: Argument.Failure(argumentRawValue)
    }
}


