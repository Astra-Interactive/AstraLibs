package ru.astrainteractive.astralibs.event

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.EventPriority
import java.util.function.Consumer

/**
 * Cold [Flow] of Forge [Event]s of [type] from [MinecraftForge.EVENT_BUS].
 * The listener is registered on collection and unregistered on cancellation.
 *
 * @param priority Defaults to [EventPriority.NORMAL].
 */
fun <T : Event> flowEvent(
    type: Class<T>,
    priority: EventPriority = EventPriority.NORMAL
): Flow<T> = callbackFlow {
    val isCancelled = false
    val consumer = Consumer<T> { event ->
        launch { send(event) }
    }
    MinecraftForge.EVENT_BUS.addListener<T>(
        priority,
        isCancelled,
        type,
        consumer
    )
    awaitClose {
        MinecraftForge.EVENT_BUS.unregister(consumer)
    }
}

/** Reified overload of [flowEvent] that infers the event type at compile time. */
inline fun <reified T : Event> flowEvent(
    priority: EventPriority = EventPriority.NORMAL
): Flow<T> = flowEvent(
    type = T::class.java,
    priority = priority
)
