package ru.astrainteractive.astralibs.command.api.registry

import org.bukkit.plugin.java.JavaPlugin

class BukkitCommandRegistryContext(val javaPlugin: JavaPlugin) : CommandRegistryContext {
    companion object {
        fun JavaPlugin.toCommandRegistryContext() = BukkitCommandRegistryContext(this)
    }
}
