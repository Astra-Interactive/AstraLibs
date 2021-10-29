package com.astrainteractive.astralibs

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

object AstraLibs {
    private lateinit var plugin: JavaPlugin
    val instance: JavaPlugin
        get() = plugin

    fun create(plugin: JavaPlugin) {
        AstraLibs.plugin = plugin
    }



    private val activeTasksList = mutableMapOf<Long,BukkitTask>()
    /**
     * Добавляем ссылка на таск
     */
    fun onBukkitTaskAdded(id: Long, taskRef: BukkitTask) {
        activeTasksList[id] = taskRef
    }

    /**
     * Отключаем таск и выкидываем его из списка
     */
    fun onBukkitTaskEnded(id: Long) {
        val task = activeTasksList[id]
        task?.cancel()
        activeTasksList.remove(id)
    }
    private fun clearAllTasks(){
        for ((key,task) in activeTasksList)
            task.cancel()
        activeTasksList.clear()

    }
}