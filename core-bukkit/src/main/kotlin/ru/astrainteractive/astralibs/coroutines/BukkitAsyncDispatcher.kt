package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
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
        val coroutineTimings = context[CoroutineTimings.Key]
        if (coroutineTimings == null) {
            plugin.server.scheduler.runTaskAsynchronously(plugin, block)
        } else {
            coroutineTimings.queue.add(block)
            plugin.server.scheduler.runTaskAsynchronously(plugin, coroutineTimings)
        }
    }
}
