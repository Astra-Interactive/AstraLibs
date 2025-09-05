package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Bukkit async dispatcher implementation which will dispatch on [BukkitScheduler.runTaskAsynchronously]
 */
class BukkitAsyncDispatcher(private val plugin: Plugin) : CoroutineDispatcher() {

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return plugin.server.isPrimaryThread && plugin.isEnabled
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!plugin.isEnabled) {
            return
        }
        val key = (context as? AbstractCoroutineContextElement)?.key as? CoroutineTimings.Key
        val timedRunnable = key?.let(context::get)
        if (timedRunnable == null) {
            plugin.server.scheduler.runTaskAsynchronously(plugin, block)
        } else {
            timedRunnable.queue.add(block)
            plugin.server.scheduler.runTaskAsynchronously(plugin, timedRunnable)
        }
    }
}
