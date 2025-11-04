package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.command.api.exception.NoPlayerException

data object OnlinePlayerArgumentConverter : ArgumentConverter<Player> {
    override fun transform(argument: String): Player {
        return Bukkit.getPlayer(argument) ?: throw NoPlayerException(argument)
    }
}
