package ru.astrainteractive.astralibs.event

import com.google.common.cache.CacheBuilder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.storage.ServerLevelData
import net.minecraftforge.event.TickEvent
import ru.astrainteractive.astralibs.server.location.KLocation
import ru.astrainteractive.klibs.mikro.core.util.tryCast
import java.util.UUID
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

/**
 * Player movement event synthesised from [TickEvent.PlayerTickEvent] snapshots, emitted by [playerMoveFlowEvent].
 *
 * @param oldKLocation Position at the previous tick.
 * @param newKLocation Position at the current tick.
 */
class PlayerMoveEvent(
    val instance: TickEvent.PlayerTickEvent,
    val oldKLocation: KLocation,
    val newKLocation: KLocation,
    val player: Player
)

/** Cold [Flow] that emits a [PlayerMoveEvent] on each tick-end position snapshot per player. */
fun playerMoveFlowEvent() = flow {
    val cache = CacheBuilder<UUID, KLocation>.newBuilder()
        .expireAfterAccess(10.seconds.toJavaDuration())
        .build<UUID, KLocation>()
    flowEvent<TickEvent.PlayerTickEvent>()
        .filter { it.phase == TickEvent.Phase.END }
        .onEach { event ->
            val player = event.player.tryCast<Player>() ?: return@onEach
            val kLocation = KLocation(
                x = event.player.x,
                y = event.player.y,
                z = event.player.z,
                worldName = event.player
                    .level()
                    .levelData
                    .tryCast<ServerLevelData>()
                    ?.levelName
                    ?: return@onEach
            )
            val cachedLocation = cache.get(event.player.uuid) {
                kLocation
            }
            val moveEvent = PlayerMoveEvent(
                instance = event,
                oldKLocation = cachedLocation,
                newKLocation = kLocation,
                player = player
            )
            emit(moveEvent)
        }.collect()
}
