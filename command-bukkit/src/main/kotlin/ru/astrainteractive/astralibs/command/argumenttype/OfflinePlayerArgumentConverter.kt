package ru.astrainteractive.astralibs.command.argumenttype

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import ru.astrainteractive.astralibs.command.argumenttype.api.ArgumentConverter
import ru.astrainteractive.astralibs.command.model.NoPlayerException

/**
 * Converts a player name to an [OfflinePlayer] from Bukkit's cache.
 *
 * @throws NoPlayerException if no cached offline player is found for [argument].
 */
public object OfflinePlayerArgumentConverter : ArgumentConverter<OfflinePlayer> {
    override fun transform(argument: String): OfflinePlayer {
        val offlinePlayer = Bukkit.getOfflinePlayer(argument)
        if (offlinePlayer.name == null) throw NoPlayerException(argument)
        return offlinePlayer
    }
}
