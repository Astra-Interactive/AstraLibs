package ru.astrainteractive.astralibs.server.util

import net.minecraft.world.level.storage.ServerLevelData
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer

fun OnlineMinecraftPlayer.asTeleportable() = Teleportable { location ->
    val player = ForgeUtil.getOnlinePlayer(uuid) ?: return@Teleportable
    val level = ForgeUtil.serverOrNull
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