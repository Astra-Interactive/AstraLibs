package ru.astrainteractive.astralibs.server.bridge

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.bridge.api.PlatformServer
import ru.astrainteractive.astralibs.server.player.BukkitKPlayer
import ru.astrainteractive.astralibs.server.player.BukkitOnlineKPlayer
import ru.astrainteractive.astralibs.server.player.api.KPlayer
import ru.astrainteractive.astralibs.server.player.api.OnlineKPlayer
import java.util.UUID

/** Bukkit implementation of [PlatformServer]. */
public class BukkitPlatformServer : PlatformServer {
    override fun getOnlinePlayers(): List<OnlineKPlayer> {
        return Bukkit
            .getOnlinePlayers()
            .map { player -> BukkitOnlineKPlayer(player) }
    }

    override fun findOnlinePlayer(uuid: UUID): OnlineKPlayer? {
        return Bukkit.getPlayer(uuid)?.let(::BukkitOnlineKPlayer)
    }

    override fun findOfflinePlayer(uuid: UUID): KPlayer {
        return BukkitKPlayer(Bukkit.getOfflinePlayer(uuid))
    }

    override fun findOnlinePlayer(name: String): OnlineKPlayer? {
        return Bukkit.getPlayer(name)?.let(::BukkitOnlineKPlayer)
    }

    override fun findOfflinePlayer(name: String): KPlayer {
        return BukkitKPlayer(Bukkit.getOfflinePlayer(name))
    }
}
