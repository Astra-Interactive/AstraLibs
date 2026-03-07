package ru.astrainteractive.astralibs.server

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.permission.BukkitPermissibleExt.toPermissible
import ru.astrainteractive.astralibs.permission.Permissible
import ru.astrainteractive.astralibs.server.player.MinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import java.util.UUID

class BukkitMinecraftNativeBridge : MinecraftNativeBridge {
    override fun OnlineMinecraftPlayerSnapshot.asAudience(): Audience {
        return OnlinePlayerAudience(this)
    }

    override fun OnlineMinecraftPlayerSnapshot.asLocatable(): Locatable {
        return OnlinePlayerLocatable(this)
    }

    override fun OnlineMinecraftPlayerSnapshot.asTeleportable(): Teleportable {
        return OnlinePlayerTeleportable(this)
    }

    override fun MinecraftPlayerSnapshot.asPermissible(): Permissible {
        val player = Bukkit.getPlayer(uuid) ?: error("Player ${this.uuid} is not online")
        return player.toPermissible()
    }

    private fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayerSnapshot? {
        val player = Bukkit.getPlayer(uuid) ?: return null
        return OnlineMinecraftPlayerSnapshot(
            uuid = player.uniqueId,
            name = player.name,
            ipAddress = player.address.hostName
        )
    }

    private fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayerSnapshot {
        val player = Bukkit.getOfflinePlayer(uuid)
        return OfflineMinecraftPlayerSnapshot(
            uuid = player.uniqueId,
        )
    }

    override fun findPlayer(uuid: UUID): MinecraftPlayerSnapshot {
        return findOnlinePlayer(uuid) ?: findOfflinePlayer(uuid)
    }
}
