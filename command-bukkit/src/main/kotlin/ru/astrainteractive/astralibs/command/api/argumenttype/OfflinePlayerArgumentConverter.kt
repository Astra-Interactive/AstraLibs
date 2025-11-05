package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import ru.astrainteractive.astralibs.command.api.exception.NoPlayerException

object OfflinePlayerArgumentConverter : ArgumentConverter<OfflinePlayer> {

    override fun transform(argument: String): OfflinePlayer {
        val offlinePlayer = Bukkit.getOfflinePlayer(argument)
        if (offlinePlayer.name == null) throw NoPlayerException(argument)
        return offlinePlayer
    }
}
