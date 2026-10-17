package ru.astrainteractive.astralibs.event

import com.google.common.cache.CacheBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.storage.ServerLevelData
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import ru.astrainteractive.astralibs.server.location.KLocation
import ru.astrainteractive.klibs.mikro.core.util.tryCast
import java.util.UUID
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

/**
 * Player movement event synthesised from [PlayerTickEvent.Post] snapshots, emitted by [playerMoveFlowEvent].
 *
 * @param oldKLocation Position at the previous tick.
 * @param newKLocation Position at the current tick.
 */
public class PlayerMoveEvent(
    public val instance: PlayerTickEvent.Post,
    public val oldKLocation: KLocation,
    public val newKLocation: KLocation,
    public val player: Player
)

/** Cold [Flow] that emits a [PlayerMoveEvent] on each tick per player. */
public fun playerMoveFlowEvent(): Flow<PlayerMoveEvent> = flow {
    val cache = CacheBuilder<UUID, KLocation>.newBuilder()
        .expireAfterAccess(10.seconds.toJavaDuration())
        .build<UUID, KLocation>()
    flowEvent<PlayerTickEvent.Post>()
        .filter { event -> event.entity is Player }
        .onEach { event ->
            val player = event.entity.tryCast<Player>() ?: return@onEach
            val kLocation = KLocation(
                x = event.entity.x,
                y = event.entity.y,
                z = event.entity.z,
                worldName = event.entity
                    .level()
                    .levelData
                    .tryCast<ServerLevelData>()
                    ?.levelName
                    ?: return@onEach
            )
            val cachedLocation = cache.get(event.entity.uuid) {
                kLocation
            }
            val event = PlayerMoveEvent(
                instance = event,
                oldKLocation = cachedLocation,
                newKLocation = kLocation,
                player = player
            )
            emit(event)
        }.collect()
}
