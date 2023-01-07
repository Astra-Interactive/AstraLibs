package ru.astrainteractive.astralibs.async

import ru.astrainteractive.astralibs.AstraLibs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import kotlin.coroutines.CoroutineContext

/**
 * Use this dispatcher to call bukkit main thread
 * For some reason it faster than [BukkitScheduler.callSyncMethod]
 */
val Dispatchers.BukkitMain: CoroutineDispatcher
    get() = BukkitMainDispatcher

object BukkitMainDispatcher : CoroutineDispatcher() {
    private val plugin: Plugin = AstraLibs.instance

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return !plugin.server.isPrimaryThread && plugin.isEnabled
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!plugin.isEnabled) {
            return
        }
        val timedRunnable = context[AsyncComponent.Key]
        if (timedRunnable == null) {
            plugin.server.scheduler.runTask(plugin, block)
        } else {
            timedRunnable.queue.add(block)
            plugin.server.scheduler.runTask(plugin, timedRunnable)
        }
    }
}

