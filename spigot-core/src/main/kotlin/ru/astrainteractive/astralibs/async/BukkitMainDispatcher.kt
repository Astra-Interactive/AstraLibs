package ru.astrainteractive.astralibs.async

import ru.astrainteractive.astralibs.AstraLibs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import kotlin.coroutines.CoroutineContext
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

