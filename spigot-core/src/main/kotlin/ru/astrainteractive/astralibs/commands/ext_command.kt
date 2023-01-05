package ru.astrainteractive.astralibs.commands

import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.AstraLibs

fun JavaPlugin.registerCommand(alias: String, block: Command.() -> Unit) {
    AstraLibs.instance.getCommand(alias)?.setExecutor { sender, _, _, args ->
        Command(alias, sender, args).apply(block)
        return@setExecutor true
    }
}

fun JavaPlugin.registerTabCompleter(alias: String, block: TabCompleter.() -> List<String>) {
    getCommand(alias)?.setTabCompleter { sender, _, _, args ->
        return@setTabCompleter block.invoke(TabCompleter(alias, sender, args))
    }
}

