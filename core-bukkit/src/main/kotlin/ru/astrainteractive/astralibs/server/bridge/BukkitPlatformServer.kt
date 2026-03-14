package ru.astrainteractive.astralibs.server.bridge

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer
import ru.astrainteractive.astralibs.server.util.asOfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer
import java.util.UUID

class BukkitPlatformServer : PlatformServer {
    override fun getOnlinePlayers(): List<OnlineKPlayer> {
        return Bukkit
            .getOnlinePlayers()
            .map { player -> player.asOnlineMinecraftPlayer() }
    }

    override fun findOnlinePlayer(uuid: UUID): OnlineKPlayer? {
        return Bukkit.getPlayer(uuid)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(uuid: UUID): KPlayer {
        return Bukkit.getOfflinePlayer(uuid).asOfflineMinecraftPlayer()
    }

    override fun findOnlinePlayer(name: String): OnlineKPlayer? {
        return Bukkit.getPlayer(name)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(name: String): KPlayer {
        return Bukkit.getOfflinePlayer(name).asOfflineMinecraftPlayer()
    }
}
