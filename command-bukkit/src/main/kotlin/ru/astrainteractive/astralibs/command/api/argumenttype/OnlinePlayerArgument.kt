package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.Bukkit
import org.bukkit.entity.Player

data object OnlinePlayerArgument : ArgumentType<Player?> {
    override fun transform(value: String?): Player? {
        return Bukkit.getOnlinePlayers().firstOrNull { it.name == value }
    }
}
