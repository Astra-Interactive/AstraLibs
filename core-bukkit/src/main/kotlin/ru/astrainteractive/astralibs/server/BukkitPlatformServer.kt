package ru.astrainteractive.astralibs.server

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asOfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer
import java.util.UUID

class BukkitPlatformServer : PlatformServer {
    override fun getOnlinePlayers(): List<OnlineMinecraftPlayer> {
        return Bukkit
            .getOnlinePlayers()
            .map { it.asOnlineMinecraftPlayer() }
    }

    override fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayer? {
        return Bukkit.getPlayer(uuid)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayer? {
        return Bukkit.getOfflinePlayer(uuid).asOfflineMinecraftPlayer()
    }

    override fun findOnlinePlayer(name: String): OnlineMinecraftPlayer? {
        return Bukkit.getPlayer(name)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(name: String): OfflineMinecraftPlayer? {
        return Bukkit.getOfflinePlayer(name).asOfflineMinecraftPlayer()
    }
}
