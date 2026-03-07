package ru.astrainteractive.astralibs.server.util

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.ServerLevelData
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import kotlin.error

fun OnlineMinecraftPlayerSnapshot.asTeleportable(): Teleportable {
    val player = NeoForgeUtil.getOnlinePlayer(uuid) ?: error("$this is not online")
    return player.asTeleportable()
}

fun ServerPlayer.asTeleportable() = Teleportable { location ->
    val player = this

    val level = player.server
        ?.allLevels
        ?.firstOrNull { (it.level.levelData as ServerLevelData).levelName == location.worldName }
        ?: return@Teleportable
    player.teleportTo(
        level,
        location.x,
        location.y,
        location.z,
        0f,
        0f
    )
}