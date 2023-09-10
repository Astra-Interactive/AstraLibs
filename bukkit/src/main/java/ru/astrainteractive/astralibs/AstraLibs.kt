@file:OptIn(UnsafeApi::class)

package ru.astrainteractive.astralibs

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.kotlin.tooling.core.UnsafeApi
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.command.registerCommand
import ru.astrainteractive.astralibs.economy.AnyEconomyProvider
import ru.astrainteractive.astralibs.event.GlobalEventListener
import ru.astrainteractive.astralibs.logging.Logger
import ru.astrainteractive.astralibs.menu.event.GlobalInventoryClickEvent
import ru.astrainteractive.astralibs.util.buildWithSpigot
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
        val economy = AnyEconomyProvider(this)
        registerCommand("test") {
            val player = sender as Player
            economy.addMoney(player.uniqueId, 100.0)
            economy.takeMoney(player.uniqueId, 100.0)
            economy.getBalance(player.uniqueId)
            economy.hasAtLeast(player.uniqueId, 100.0)
        }
    }

    override fun onDisable() {
        super.onDisable()
        PluginScope.close()
        GlobalEventListener.onDisable()
        GlobalInventoryClickEvent.onDisable()
    }
}
