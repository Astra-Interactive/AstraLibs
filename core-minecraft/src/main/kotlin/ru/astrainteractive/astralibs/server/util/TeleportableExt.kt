package ru.astrainteractive.astralibs.server.util

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.ServerLevelData
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.klibs.mikro.core.util.tryCast

/**
 * Wraps this [ServerPlayer] as a [Teleportable] that teleports the player to a [KLocation].
 */
fun ServerPlayer.asTeleportable() = Teleportable { location ->
    val player = this
    val level = MinecraftUtil.serverOrNull
        ?.allLevels
        ?.firstOrNull { serverLevel ->
            serverLevel.level.levelData.tryCast<ServerLevelData>()?.levelName == location.worldName
        }
        ?: return@Teleportable
    player.teleportTo(
        level,
        location.x,
        location.y,
        location.z,
        0f,
        0f,
    )
}
