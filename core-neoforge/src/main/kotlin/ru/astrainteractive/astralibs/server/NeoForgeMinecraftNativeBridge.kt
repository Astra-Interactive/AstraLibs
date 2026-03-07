package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.permission.LuckPermsPermissible
import ru.astrainteractive.astralibs.permission.Permissible
import ru.astrainteractive.astralibs.server.player.MinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.util.NeoForgeUtil
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayer
import java.util.UUID

class NeoForgeMinecraftNativeBridge : MinecraftNativeBridge {
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
        return LuckPermsPermissible(uuid)
    }

    private fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayerSnapshot? {
        val player = NeoForgeUtil.getOnlinePlayer(uuid) ?: return null
        return player.asOnlineMinecraftPlayer()
    }

    private fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayerSnapshot? {
        val player = NeoForgeUtil.getOnlinePlayer(uuid) ?: return null
        return OfflineMinecraftPlayerSnapshot(
            uuid = player.uuid,
        )
    }

    override fun findPlayer(uuid: UUID): MinecraftPlayerSnapshot? {
        return findOnlinePlayer(uuid) ?: findOfflinePlayer(uuid)
    }
}
