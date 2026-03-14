package ru.astrainteractive.astralibs.server.bridge

import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer
import java.util.UUID

interface PlatformServer {
    fun getOnlinePlayers(): List<OnlineKPlayer>

    fun findOnlinePlayer(uuid: UUID): OnlineKPlayer?

    fun findOfflinePlayer(uuid: UUID): KPlayer?

    fun findOnlinePlayer(name: String): OnlineKPlayer?

    fun findOfflinePlayer(name: String): KPlayer?
}
