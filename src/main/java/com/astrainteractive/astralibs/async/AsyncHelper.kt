package com.astrainteractive.astralibs.async

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.utils.catching
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import kotlin.coroutines.CoroutineContext

/**
 * If you don't want to extend your class, just use [AsyncHelper.launch]
 */
object AsyncHelper : AsyncTask

val Dispatchers.BukkitMain: CoroutineDispatcher
    get() = BukkitMainDispatcher

object BukkitMainDispatcher : CoroutineDispatcher() {
    private val plugin: Plugin = AstraLibs.instance
    private val bukkitScheduler
        get() = Bukkit.getScheduler()

    private val runTask: (Plugin, Runnable) -> BukkitTask = bukkitScheduler::runTask


    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!context.isActive) return

        if (Bukkit.isPrimaryThread()) block.run()
        else runTask(plugin, block)
    }

}

