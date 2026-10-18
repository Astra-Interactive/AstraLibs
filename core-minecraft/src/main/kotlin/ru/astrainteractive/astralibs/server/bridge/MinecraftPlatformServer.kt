package ru.astrainteractive.astralibs.server.bridge

import ru.astrainteractive.astralibs.server.api.MinecraftServerRegistry
import ru.astrainteractive.astralibs.server.bridge.api.PlatformServer
import ru.astrainteractive.astralibs.server.player.MinecraftKPlayer
import ru.astrainteractive.astralibs.server.player.MinecraftOnlineKPlayer
import ru.astrainteractive.astralibs.server.player.api.KPlayer
import ru.astrainteractive.astralibs.server.player.api.OnlineKPlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayer
import ru.astrainteractive.astralibs.server.util.getOnlinePlayers
import ru.astrainteractive.astralibs.server.util.getPlayerGameProfile
import java.util.UUID

/**
 * Vanilla-Minecraft [PlatformServer] implementation backed by [MinecraftServerRegistry].
 * Offline-player resolution only works for profiles cached locally on this server.
 */
public object MinecraftPlatformServer : PlatformServer {
    override fun getOnlinePlayers(): List<OnlineKPlayer> {
        return MinecraftServerRegistry
            .getOnlinePlayers()
            .map { serverPlayer -> MinecraftOnlineKPlayer(serverPlayer) }
    }

    override fun findOnlinePlayer(uuid: UUID): OnlineKPlayer? {
        return MinecraftServerRegistry.getOnlinePlayer(uuid)?.let(::MinecraftOnlineKPlayer)
    }

    override fun findOfflinePlayer(uuid: UUID): KPlayer? {
        return MinecraftServerRegistry.getPlayerGameProfile(uuid)?.let(::MinecraftKPlayer)
    }

    override fun findOnlinePlayer(name: String): OnlineKPlayer? {
        return MinecraftServerRegistry.getOnlinePlayer(name)?.let(::MinecraftOnlineKPlayer)
    }

    override fun findOfflinePlayer(name: String): KPlayer? {
        return MinecraftServerRegistry.getPlayerGameProfile(name)?.let(::MinecraftKPlayer)
    }
}
