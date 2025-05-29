package ru.astrainteractive.astralibs.server

import java.util.UUID
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.ForgeUtil
import ru.astrainteractive.astralibs.server.util.asOfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayers
import ru.astrainteractive.astralibs.server.util.getPlayerGameProfile

object ForgePlatformServer : PlatformServer {
    override fun getOnlinePlayers(): List<OnlineMinecraftPlayer> {
        return ForgeUtil
            .getOnlinePlayers()
            .map { serverPlayer -> serverPlayer.asOnlineMinecraftPlayer() }
    }

    override fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayer? {
        return ForgeUtil.getOnlinePlayer(uuid)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayer? {
        return ForgeUtil.getPlayerGameProfile(uuid)?.asOfflineMinecraftPlayer()
    }

    override fun findOnlinePlayer(name: String): OnlineMinecraftPlayer? {
        return ForgeUtil.getOnlinePlayer(name)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(name: String): OfflineMinecraftPlayer? {
        return ForgeUtil.getPlayerGameProfile(name)?.asOfflineMinecraftPlayer()
    }
}
