package ru.astrainteractive.astralibs.server

import java.util.UUID
import ru.astrainteractive.astralibs.permission.ForgeLuckPermsPlayerPermissible
import ru.astrainteractive.astralibs.permission.Permissible
import ru.astrainteractive.astralibs.server.player.MinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.ForgeUtil
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayer

class ForgeMinecraftNativeBridge : MinecraftNativeBridge {
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
        return ForgeLuckPermsPlayerPermissible(uuid)
    }

    private fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayer? {
        val player = ForgeUtil.getOnlinePlayer(uuid) ?: return null
        return player.asOnlineMinecraftPlayer()
    }

    private fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayer? {
        val player = ForgeUtil.getOnlinePlayer(uuid) ?: return null
        return OfflineMinecraftPlayer(
            uuid = player.uuid,
        )
    }

    override fun findPlayer(uuid: UUID): MinecraftPlayer? {
        return findOnlinePlayer(uuid) ?: findOfflinePlayer(uuid)
    }
}
