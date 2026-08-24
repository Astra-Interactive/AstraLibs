package ru.astrainteractive.astralibs.event

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.EventPriority
import net.neoforged.neoforge.common.NeoForge
import java.lang.reflect.Modifier
import java.util.function.Consumer

/**
 * NeoForge EventBus 8+ refuses listeners on abstract event classes and never
 * dispatches a concrete event to listeners of its abstract parents, so an
 * abstract [type] is expanded into its concrete nested subclasses
 * (e.g. ServerTickEvent -> [Pre, Post], PlayerInteractEvent -> [RightClickBlock, ...]).
 */
@Suppress("UNCHECKED_CAST")
internal fun <T : Event> concreteEventClasses(type: Class<T>): List<Class<out T>> {
    if (!Modifier.isAbstract(type.modifiers)) return listOf(type)
    return type.declaredClasses
        .filter { nestedClass -> type.isAssignableFrom(nestedClass) }
        .map { nestedClass -> nestedClass as Class<out T> }
        .flatMap { nestedClass -> concreteEventClasses(nestedClass) }
}

/**
 * Cold [Flow] of NeoForge [Event]s of [type] from [NeoForge.EVENT_BUS].
 * The listener is registered on collection and unregistered on cancellation.
 *
 * Abstract [type]s are listened to via their concrete nested subclasses. An abstract
 * [type] whose concrete subclasses are declared outside of it (e.g. EntityEvent)
 * cannot be expanded and must be subscribed to via a concrete subclass instead.
 *
 * @param priority Defaults to [EventPriority.NORMAL].
 */
@Suppress("UNCHECKED_CAST")
fun <T : Event> flowEvent(
    type: Class<T>,
    priority: EventPriority = EventPriority.NORMAL
): Flow<T> = callbackFlow {
    val isCancelled = false
    val consumer = Consumer<T> { event ->
        launch { send(event) }
    }
    val concreteTypes = concreteEventClasses(type)
    require(concreteTypes.isNotEmpty()) {
        "$type is abstract and declares no concrete nested subclasses. " +
            "NeoForge EventBus dispatches only concrete events - subscribe to a concrete subclass of $type instead"
    }
    concreteTypes.forEach { concreteType ->
        NeoForge.EVENT_BUS.addListener(
            priority,
            isCancelled,
            concreteType as Class<T>,
            consumer
        )
    }
    awaitClose {
        NeoForge.EVENT_BUS.unregister(consumer)
    }
}

/** Reified overload of [flowEvent] that infers the event class at compile time. */
inline fun <reified T : Event> flowEvent(
    priority: EventPriority = EventPriority.NORMAL
): Flow<T> = flowEvent(
    type = T::class.java,
    priority = priority
)
