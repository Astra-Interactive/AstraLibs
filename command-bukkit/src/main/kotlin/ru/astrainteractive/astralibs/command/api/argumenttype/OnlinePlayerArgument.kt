package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.command.api.exception.NoPlayerException

data object OnlinePlayerArgument : ArgumentType<Player> {
    override val key: String = "OnlinePlayerArgument"
    override fun transform(value: String): Player {
        return Bukkit.getOnlinePlayers()
            .firstOrNull { it.name == value }
            ?: throw NoPlayerException(value)
    }
}
