package ru.astrainteractive.astralibs.server

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.util.asOfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer
import java.util.UUID

class BukkitPlatformServer : PlatformServer {
    override fun getOnlinePlayers(): List<OnlineMinecraftPlayerSnapshot> {
        return Bukkit
            .getOnlinePlayers()
            .map { it.asOnlineMinecraftPlayer() }
    }

    override fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayerSnapshot? {
        return Bukkit.getPlayer(uuid)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayerSnapshot? {
        return Bukkit.getOfflinePlayer(uuid).asOfflineMinecraftPlayer()
    }

    override fun findOnlinePlayer(name: String): OnlineMinecraftPlayerSnapshot? {
        return Bukkit.getPlayer(name)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(name: String): OfflineMinecraftPlayerSnapshot? {
        return Bukkit.getOfflinePlayer(name).asOfflineMinecraftPlayer()
    }
}
