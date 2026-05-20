package ru.astrainteractive.astralibs.server.bridge

import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer
import ru.astrainteractive.astralibs.server.util.MinecraftUtil
import ru.astrainteractive.astralibs.server.util.asOfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.asOnlineMinecraftPlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayers
import ru.astrainteractive.astralibs.server.util.getPlayerGameProfile
import java.util.UUID

/**
 * Vanilla-Minecraft [PlatformServer] implementation backed by [MinecraftUtil].
 * Offline-player resolution only works for profiles cached locally on this server.
 */
object MinecraftPlatformServer : PlatformServer {
    override fun getOnlinePlayers(): List<OnlineKPlayer> {
        return MinecraftUtil
            .getOnlinePlayers()
            .map { serverPlayer -> serverPlayer.asOnlineMinecraftPlayer() }
    }

    override fun findOnlinePlayer(uuid: UUID): OnlineKPlayer? {
        return MinecraftUtil.getOnlinePlayer(uuid)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(uuid: UUID): KPlayer? {
        return MinecraftUtil.getPlayerGameProfile(uuid)?.asOfflineMinecraftPlayer()
    }

    override fun findOnlinePlayer(name: String): OnlineKPlayer? {
        return MinecraftUtil.getOnlinePlayer(name)?.asOnlineMinecraftPlayer()
    }

    override fun findOfflinePlayer(name: String): KPlayer? {
        return MinecraftUtil.getPlayerGameProfile(name)?.asOfflineMinecraftPlayer()
    }
}
