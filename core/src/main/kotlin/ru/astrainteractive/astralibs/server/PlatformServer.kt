package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import java.util.UUID

interface PlatformServer {
    fun getOnlinePlayers(): List<OnlineMinecraftPlayer>

    fun findOnlinePlayer(uuid: UUID): OnlineMinecraftPlayer?

    fun findOfflinePlayer(uuid: UUID): OfflineMinecraftPlayer?

    fun findOnlinePlayer(name: String): OnlineMinecraftPlayer?

    fun findOfflinePlayer(name: String): OfflineMinecraftPlayer?
}
