package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.util.NeoForgeUtil
import ru.astrainteractive.astralibs.server.util.asOfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayers
import ru.astrainteractive.astralibs.server.util.getPlayerGameProfile
import java.util.UUID

object NeoForgePlatformServer : PlatformServer {
    override fun getOnlinePlayers(): List<OnlineMinecraftPlayerSnapshot> {
        return NeoForgeUtil
            .getOnlinePlayers()
            .map { serverPlayer -> serverPlayer.asOnlineMinecraftPlayer() }
    }

    override fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayerSnapshot? {
        return NeoForgeUtil.getOnlinePlayer(uuid)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayerSnapshot? {
        return NeoForgeUtil.getPlayerGameProfile(uuid)?.asOfflineMinecraftPlayer()
    }

    override fun findOnlinePlayer(name: String): OnlineMinecraftPlayerSnapshot? {
        return NeoForgeUtil.getOnlinePlayer(name)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(name: String): OfflineMinecraftPlayerSnapshot? {
        return NeoForgeUtil.getPlayerGameProfile(name)?.asOfflineMinecraftPlayer()
    }
}
