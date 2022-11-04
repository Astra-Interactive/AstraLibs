package ru.astrainteractive.astralibs.commands

import it.unimi.dsi.fastutil.shorts.ShortRBTreeSet
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.utils.registerCommand



class Argument<T>(val value: T, val rawValue: String?)

class Command(val alias: String, val sender: CommandSender, val args: Array<out String>) {

    fun <T> argument(
        index: Int,
        parser: (String?) -> T?,
        onError: (String?) -> Unit = {},
        onResult: (Argument<T>) -> Unit
    ) {
        val argumentRawValue = args.getOrNull(index)
        val argumentValue = parser.invoke(argumentRawValue)
        argumentValue?.let { onResult(Argument(it, argumentRawValue)) }
            ?: onError(argumentRawValue)
    }
}


object DSLCommand {

    operator fun invoke(alias: String, block: Command.() -> Unit) =
        AstraLibs.registerCommand(alias) { sender, args ->
            Command(alias, sender, args).apply(block)
        }
}