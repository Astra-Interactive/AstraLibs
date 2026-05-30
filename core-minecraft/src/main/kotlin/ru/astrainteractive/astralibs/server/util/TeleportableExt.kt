package ru.astrainteractive.astralibs.server.util

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.TicketType
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.storage.ServerLevelData
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.klibs.mikro.core.util.tryCast

/**
 * Wraps this [ServerPlayer] as a [Teleportable] that teleports the player to a [KLocation].
 */
fun ServerPlayer.asTeleportable() = Teleportable { location ->
    val player = this
    val server = MinecraftUtil.serverOrNull ?: return@Teleportable
    val level = server
        .allLevels
        .firstOrNull { serverLevel ->
            serverLevel.level.levelData.tryCast<ServerLevelData>()?.levelName == location.worldName
        }
        ?: return@Teleportable
    val chunkPos = ChunkPos(BlockPos.containing(location.x, location.y, location.z))
    level.chunkSource.addRegionTicket(
        TicketType.POST_TELEPORT,
        chunkPos,
        server.playerList.simulationDistance,
        player.id
    )
    player.teleportTo(
        level,
        location.x,
        location.y,
        location.z,
        0f,
        0f,
    )
}
