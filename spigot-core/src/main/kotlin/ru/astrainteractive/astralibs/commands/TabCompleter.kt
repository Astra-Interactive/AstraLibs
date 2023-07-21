package ru.astrainteractive.astralibs.commands

import org.bukkit.command.CommandSender
import ru.astrainteractive.astralibs.utils.withEntry

class TabCompleter(val alias: String, val sender: CommandSender, val args: Array<out String>) {
    private val set = HashMap<Int, List<String>>()

    fun label(index: Int, block: () -> List<String>) {
        val argument = args.getOrNull(index)
        set[index + 1] = block.invoke().withEntry(argument)
    }

    fun predicate(index: Int, alias: String?, block: () -> Unit) {
        if (args.getOrNull(index) == alias) {
            block.invoke()
        } else {
            println("${args.getOrNull(index)} != $alias")
        }
    }

    fun hints() = set[args.size].orEmpty()
}
