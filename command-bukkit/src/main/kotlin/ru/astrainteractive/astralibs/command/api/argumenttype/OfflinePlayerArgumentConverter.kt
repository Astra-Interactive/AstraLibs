package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import ru.astrainteractive.astralibs.command.api.exception.NoPlayerException

/**
 * Converter that transforms a string argument into an OfflinePlayer instance.
 *
 * This converter is responsible for taking a string argument, which represents a player's name or UUID,
 * and returning the corresponding OfflinePlayer object. If the player does not exist in the server's
 * offline player cache, it throws a NoPlayerException.
 */
object OfflinePlayerArgumentConverter : ArgumentConverter<OfflinePlayer> {

    /**
     * Converts a string argument into an OfflinePlayer instance.
     *
     * @param argument The string representation of a player's name or UUID.
     * @return The corresponding OfflinePlayer object.
     * @throws NoPlayerException If the player does not exist in the server's offline player cache.
     */
    override fun transform(argument: String): OfflinePlayer {
        val offlinePlayer = Bukkit.getOfflinePlayer(argument)
        if (offlinePlayer.name == null) throw NoPlayerException(argument)
        return offlinePlayer
    }
}
