package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import ru.astrainteractive.astralibs.command.api.exception.BukkitCommandException

object OfflinePlayerArgument : ArgumentType<OfflinePlayer> {
    override val key: String = "OfflinePlayerArgument"

    override fun transform(value: String): OfflinePlayer {
        val offlinePlayer = Bukkit.getOfflinePlayer(value)
        if (offlinePlayer.name == null) throw BukkitCommandException.NoPlayerException(value)
        return offlinePlayer
    }
}
