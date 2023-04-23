@file:OptIn(UnsafeApi::class)

package ru.astrainteractive.astralibs

import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.kotlin.tooling.core.UnsafeApi
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.di.Singleton
import ru.astrainteractive.astralibs.events.GlobalEventListener
import ru.astrainteractive.astralibs.logging.Logger
import ru.astrainteractive.astralibs.menu.event.GlobalInventoryClickEvent
import ru.astrainteractive.astralibs.utils.setupWithSpigot

class AstraLibs : JavaPlugin() {
    companion object : Singleton<AstraLibs>()

    override fun onEnable() {
        super.onEnable()
        AstraLibs.instance = this
        Logger.setupWithSpigot("AstraLibs", this)
    }

    override fun onDisable() {
        super.onDisable()
        PluginScope.close()
        GlobalEventListener.onDisable()
        GlobalInventoryClickEvent.onDisable()
    }

}

