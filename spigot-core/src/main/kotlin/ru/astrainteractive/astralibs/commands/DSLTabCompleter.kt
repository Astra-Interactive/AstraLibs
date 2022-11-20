package ru.astrainteractive.astralibs.commands

import org.bukkit.command.CommandSender
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.utils.registerTabCompleter

class TabCompleter(val alias: String, val sender: CommandSender, val args: Array<out String>) {

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


object DSLTabCompleter {
    operator fun invoke(alias: String, block: TabCompleter.() -> List<String>) =
        AstraLibs.registerTabCompleter(alias) { sender, args ->
            return@registerTabCompleter block.invoke(TabCompleter(alias, sender, args))

        }
}