@file:OptIn(UnsafeApi::class)

package ru.astrainteractive.astralibs

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.kotlin.tooling.core.UnsafeApi
import ru.astrainteractive.astralibs.async.AsyncComponent
import ru.astrainteractive.astralibs.command.registerCommand
import ru.astrainteractive.astralibs.economy.AnyEconomyProvider
import ru.astrainteractive.astralibs.event.EventListener
import ru.astrainteractive.astralibs.logging.Logger
import ru.astrainteractive.astralibs.menu.event.DefaultInventoryClickEvent
import ru.astrainteractive.astralibs.serialization.KyoriComponentSerializer
import ru.astrainteractive.astralibs.util.buildWithSpigot
import ru.astrainteractive.klibs.kdi.Lateinit
import ru.astrainteractive.klibs.kdi.Module

class AstraLibs : JavaPlugin() {
    companion object : Module {
        val instance = Lateinit<AstraLibs>()
        val logger = Lateinit<Logger>()
        val pluginScope = AsyncComponent.Default()
        val inventoryClickEvent = DefaultInventoryClickEvent()
        val eventListener = EventListener.Default()
    }

    override fun onEnable() {
        super.onEnable()

        this.also(AstraLibs.instance::initialize)
        Logger.buildWithSpigot("AstraLibs", this).also(AstraLibs.logger::initialize)
        val economy = AnyEconomyProvider(this)
        registerCommand("test") {
            val player = sender as Player
            KyoriComponentSerializer.Legacy.toComponent("&2Hello!").run(player::sendMessage)
            KyoriComponentSerializer.Legacy.toComponent("§2Hello!").run(player::sendMessage)
            KyoriComponentSerializer.Legacy.toComponent("&#42f5c5Hello!").run(player::sendMessage)
            KyoriComponentSerializer.Legacy.toComponent("#42f5c5Hello!").run(player::sendMessage)
            KyoriComponentSerializer.Legacy.toComponent("&2Hello!").content().run(::println)
            KyoriComponentSerializer.Legacy.toComponent("§2Hello!").content().run(::println)
            KyoriComponentSerializer.Legacy.toComponent("&#000000Hello!").content().run(::println)
        }
    }

    override fun onDisable() {
        super.onDisable()
        pluginScope.close()
        eventListener.onDisable()
        inventoryClickEvent.onDisable()
    }
}
