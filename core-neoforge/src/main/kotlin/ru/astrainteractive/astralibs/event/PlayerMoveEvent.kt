package ru.astrainteractive.astralibs.event

import com.google.common.cache.CacheBuilder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.storage.ServerLevelData
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import ru.astrainteractive.astralibs.server.location.Location
import ru.astrainteractive.klibs.mikro.core.util.cast
import ru.astrainteractive.klibs.mikro.core.util.tryCast
import java.util.UUID
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class PlayerMoveEvent(
    val instance: PlayerTickEvent.Post,
    val oldLocation: Location,
    val newLocation: Location,
    val player: Player
)

fun playerMoveFlowEvent() = flow {
    val cache = CacheBuilder<UUID, Location>
        .newBuilder()
        .expireAfterAccess(10.seconds.toJavaDuration())
        .build<UUID, Location>()
    flowEvent<PlayerTickEvent.Post>()
        .filter { it.entity is Player }
        .onEach { event ->
            val player = event.entity.tryCast<Player>() ?: return@onEach
            val location = Location(
                x = event.entity.x,
                y = event.entity.y,
                z = event.entity.z,
                worldName = event.entity
                    .level()
                    .levelData
                    .cast<ServerLevelData>()
                    .levelName
            )
            val cachedLocation = cache.get(event.entity.uuid) {
                location
            }
            val event = PlayerMoveEvent(
                instance = event,
                oldLocation = cachedLocation,
                newLocation = location,
                player = player
            )
            emit(event)
        }.collect()
}
