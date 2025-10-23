package ru.astrainteractive.astralibs.event

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.Plugin

fun <T : Registrar> Plugin.flowLifecycleEvent(
    eventType: LifecycleEventType.Prioritizable<LifecycleEventOwner, ReloadableRegistrarEvent<T>>
): Flow<ReloadableRegistrarEvent<T>> = callbackFlow {
    val coroutineContext = currentCoroutineContext()
    val handler = LifecycleEventHandler<ReloadableRegistrarEvent<T>> { event ->
        if (!coroutineContext.isActive) return@LifecycleEventHandler
        runBlocking { trySendBlocking(event) }
    }
    this@flowLifecycleEvent.lifecycleManager.registerEventHandler(eventType, handler)
    awaitClose {
        // wtf no unregister method
    }
}
