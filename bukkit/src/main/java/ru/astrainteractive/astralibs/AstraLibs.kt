@file:OptIn(UnsafeApi::class)

package ru.astrainteractive.astralibs

import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.kotlin.tooling.core.UnsafeApi
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.events.GlobalEventListener
import ru.astrainteractive.astralibs.logging.Logger
import ru.astrainteractive.astralibs.menu.event.GlobalInventoryClickEvent
import ru.astrainteractive.astralibs.utils.buildWithSpigot
import ru.astrainteractive.klibs.kdi.Lateinit
import ru.astrainteractive.klibs.kdi.Module

class AstraLibs : JavaPlugin() {
    companion object : Module {
        val instance = Lateinit<AstraLibs>()
        val logger = Lateinit<Logger>()
    }

    override fun onEnable() {
        super.onEnable()

        this.also(AstraLibs.instance::initialize)
        Logger.buildWithSpigot("AstraLibs", this).also(AstraLibs.logger::initialize)
    }

    override fun onDisable() {
        super.onDisable()
        PluginScope.close()
        GlobalEventListener.onDisable()
        GlobalInventoryClickEvent.onDisable()
    }
}
