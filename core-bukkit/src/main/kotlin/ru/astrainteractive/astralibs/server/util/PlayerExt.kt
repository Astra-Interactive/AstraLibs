package ru.astrainteractive.astralibs.server.util

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer

fun Player.asOnlineMinecraftPlayer(): OnlineMinecraftPlayer {
    return OnlineMinecraftPlayer(
        uuid = uniqueId,
        name = name,
        ipAddress = address?.hostName.orEmpty()
    )
}

fun OfflinePlayer.asOfflineMinecraftPlayer(): OfflineMinecraftPlayer {
    return OfflineMinecraftPlayer(
        uuid = uniqueId,
    )
}
