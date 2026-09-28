package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.server.bridge.PlatformServer
import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer
import java.util.UUID

/** A server that knows [offlinePlayers] and has [onlinePlayers] connected; online players are known too. */
internal class FakePlatformServer(
    private val onlinePlayers: List<OnlineKPlayer>,
    private val offlinePlayers: List<KPlayer>
) : PlatformServer {
    private val knownPlayers: List<KPlayer> = onlinePlayers + offlinePlayers

    override fun getOnlinePlayers(): List<OnlineKPlayer> = onlinePlayers

    override fun findOnlinePlayer(uuid: UUID): OnlineKPlayer? {
        return onlinePlayers.firstOrNull { player -> player.uuid == uuid }
    }

    override fun findOfflinePlayer(uuid: UUID): KPlayer? {
        return knownPlayers.firstOrNull { player -> player.uuid == uuid }
    }

    override fun findOnlinePlayer(name: String): OnlineKPlayer? {
        return onlinePlayers.firstOrNull { player -> player.name == name }
    }

    override fun findOfflinePlayer(name: String): KPlayer? {
        return knownPlayers.firstOrNull { player -> player.name == name }
    }
}
