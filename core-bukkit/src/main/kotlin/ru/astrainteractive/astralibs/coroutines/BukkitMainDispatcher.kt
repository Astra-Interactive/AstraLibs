package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.MainCoroutineDispatcher
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import kotlin.coroutines.CoroutineContext

/**
 * Bukkit main dispatcher implementation which will dispatch on [BukkitScheduler.runTask]
 */
class BukkitMainDispatcher(private val plugin: Plugin) : MainCoroutineDispatcher() {

    override val immediate: MainCoroutineDispatcher
        get() = this

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return !plugin.server.isPrimaryThread && plugin.isEnabled
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!plugin.isEnabled) {
            return
        }
        val coroutineTimings = context[CoroutineTimings.Key]
        if (coroutineTimings == null) {
            plugin.server.scheduler.runTask(plugin, block)
        } else {
            coroutineTimings.queue.add(block)
            plugin.server.scheduler.runTask(plugin, coroutineTimings)
        }
    }
}
