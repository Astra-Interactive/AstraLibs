package ru.astrainteractive.astralibs.server.util

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.player.BukkitKPlayer
import ru.astrainteractive.astralibs.server.player.BukkitOnlineKPlayer
import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

fun Player.asOnlineMinecraftPlayer(): OnlineKPlayer {
    return BukkitOnlineKPlayer(instance = this)
}

fun OfflinePlayer.asOfflineMinecraftPlayer(): KPlayer {
    return BukkitKPlayer(instance = this)
}
