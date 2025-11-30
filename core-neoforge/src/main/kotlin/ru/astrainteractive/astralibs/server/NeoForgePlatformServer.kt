package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.NeoForgeUtil
import ru.astrainteractive.astralibs.server.util.asOfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayers
import ru.astrainteractive.astralibs.server.util.getPlayerGameProfile
import java.util.UUID

object NeoForgePlatformServer : PlatformServer {
    override fun getOnlinePlayers(): List<OnlineMinecraftPlayer> {
        return NeoForgeUtil
            .getOnlinePlayers()
            .map { serverPlayer -> serverPlayer.asOnlineMinecraftPlayer() }
    }

    override fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayer? {
        return NeoForgeUtil.getOnlinePlayer(uuid)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayer? {
        return NeoForgeUtil.getPlayerGameProfile(uuid)?.asOfflineMinecraftPlayer()
    }

    override fun findOnlinePlayer(name: String): OnlineMinecraftPlayer? {
        return NeoForgeUtil.getOnlinePlayer(name)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(name: String): OfflineMinecraftPlayer? {
        return NeoForgeUtil.getPlayerGameProfile(name)?.asOfflineMinecraftPlayer()
    }
}
