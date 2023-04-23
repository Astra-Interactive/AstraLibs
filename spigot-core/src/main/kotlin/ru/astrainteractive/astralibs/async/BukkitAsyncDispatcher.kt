package ru.astrainteractive.astralibs.async


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import ru.astrainteractive.astralibs.AstraLibs
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
        val timedRunnable = context[AsyncComponent.Key]
        if (timedRunnable == null) {
            plugin.server.scheduler.runTaskAsynchronously(plugin, block)
        } else {
            timedRunnable.queue.add(block)
            plugin.server.scheduler.runTaskAsynchronously(plugin, timedRunnable)
        }
    }
}

