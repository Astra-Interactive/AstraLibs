package ru.astrainteractive.astralibs.server.util

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot

fun Player.asOnlineMinecraftPlayer(): OnlineMinecraftPlayerSnapshot {
    return OnlineMinecraftPlayerSnapshot(
        uuid = uniqueId,
        name = name,
        ipAddress = address?.hostName.orEmpty()
    )
}

fun OfflinePlayer.asOfflineMinecraftPlayer(): OfflineMinecraftPlayerSnapshot {
    return OfflineMinecraftPlayerSnapshot(
        uuid = uniqueId,
    )
}
