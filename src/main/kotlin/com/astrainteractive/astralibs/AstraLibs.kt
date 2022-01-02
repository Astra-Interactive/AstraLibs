package com.astrainteractive.astralibs

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

object AstraLibs {
    private lateinit var plugin: JavaPlugin
    val instance: JavaPlugin
        get() = plugin

    /**
     * Initializer for AstraLibs
     */
    fun create(plugin: JavaPlugin) {
        AstraLibs.plugin = plugin
    }



    private val activeTasksList = mutableMapOf<Long,BukkitTask>()
    /**
     * Add task to list
     */
    fun onBukkitTaskAdded(id: Long, taskRef: BukkitTask) {
        activeTasksList[id] = taskRef
    }

    /**
     * Disable task and remove from list
     */
    fun onBukkitTaskEnded(id: Long) {
        val task = activeTasksList[id]
        task?.cancel()
        activeTasksList.remove(id)
    }

    /**
     * Clear all tasks
     */
    fun clearAllTasks(){
        activeTasksList.forEach { (_,task) ->
            catchingNoStackTrace{
                task.cancel()
            }
        }
        activeTasksList.clear()

    }
}