package ru.astrainteractive.astralibs.server

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.permission.BukkitPermissibleExt.toPermissible
import ru.astrainteractive.astralibs.permission.Permissible
import java.util.UUID
import ru.astrainteractive.astralibs.server.player.MinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer

class BukkitMinecraftNativeBridge : MinecraftNativeBridge {
    override fun OnlineMinecraftPlayer.asAudience(): Audience {
        return OnlinePlayerAudience(this)
    }

    override fun OnlineMinecraftPlayer.asLocatable(): Locatable {
        return OnlinePlayerLocatable(this)
    }

    override fun OnlineMinecraftPlayer.asTeleportable(): Teleportable {
        return OnlinePlayerTeleportable(this)
    }

    override fun MinecraftPlayer.asPermissible(): Permissible {
        val player = Bukkit.getPlayer(uuid) ?: error("Player ${this.uuid} is not online")
        return player.toPermissible()
    }

    private fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayer? {
        val player = Bukkit.getPlayer(uuid) ?: return null
        return OnlineMinecraftPlayer(
            uuid = player.uniqueId,
            name = player.name,
            ipAddress = player.address.hostName
        )
    }

    private fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayer {
        val player = Bukkit.getOfflinePlayer(uuid)
        return OfflineMinecraftPlayer(
            uuid = player.uniqueId,
        )
    }

    override fun findPlayer(uuid: UUID): MinecraftPlayer {
        return findOnlinePlayer(uuid) ?: findOfflinePlayer(uuid)
    }
}
