package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import java.util.UUID

interface PlatformServer {
    fun getOnlinePlayers(): List<OnlineMinecraftPlayerSnapshot>

    fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayerSnapshot?

    fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayerSnapshot?

    fun findOnlinePlayer(name: String): OnlineMinecraftPlayerSnapshot?

    fun findOfflinePlayer(name: String): OfflineMinecraftPlayerSnapshot?
}
