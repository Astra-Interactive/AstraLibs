package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.command.api.exception.NoPlayerException

/**
 * Converts a player name argument into an online [Player].
 *
 * @throws NoPlayerException if no online player with that name exists.
 * @see OfflinePlayerArgumentConverter
 */
data object OnlinePlayerArgumentConverter : ArgumentConverter<Player> {
    override fun transform(argument: String): Player {
        return Bukkit.getPlayer(argument) ?: throw NoPlayerException(argument)
    }
}
