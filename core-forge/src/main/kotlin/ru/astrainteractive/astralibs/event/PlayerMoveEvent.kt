package ru.astrainteractive.astralibs.event

import com.google.common.cache.CacheBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.storage.ServerLevelData
import net.minecraftforge.event.TickEvent
import ru.astrainteractive.astralibs.event.util.flowEvent
import ru.astrainteractive.astralibs.server.location.model.KLocation
import ru.astrainteractive.klibs.mikro.core.util.tryCast
import java.util.UUID
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

/**
 * Player movement event synthesised from [TickEvent.PlayerTickEvent] snapshots, emitted by [playerMoveFlowEvent].
 *
 * @property oldKLocation Position at the previous tick.
 * @property newKLocation Position at the current tick.
 */
public interface PlayerMoveEvent {
    public val instance: TickEvent.PlayerTickEvent
    public val oldKLocation: KLocation
    public val newKLocation: KLocation
    public val player: Player
}

internal class TickPlayerMoveEvent(
    override val instance: TickEvent.PlayerTickEvent,
    override val oldKLocation: KLocation,
    override val newKLocation: KLocation,
    override val player: Player
) : PlayerMoveEvent

/** Cold [Flow] that emits a [PlayerMoveEvent] on each tick-end position snapshot per player. */
public fun playerMoveFlowEvent(): Flow<PlayerMoveEvent> = flow {
    val cache = CacheBuilder<UUID, KLocation>.newBuilder()
        .expireAfterAccess(10.seconds.toJavaDuration())
        .build<UUID, KLocation>()
    flowEvent<TickEvent.PlayerTickEvent>()
        .filter { event -> event.phase == TickEvent.Phase.END }
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
            val moveEvent = TickPlayerMoveEvent(
                instance = event,
                oldKLocation = cachedLocation,
                newKLocation = kLocation,
                player = player
            )
            emit(moveEvent)
        }.collect()
}
