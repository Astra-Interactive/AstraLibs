package ru.astrainteractive.astralibs

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.async.AsyncComponent
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
    }

    override fun onEnable() {
        super.onEnable()

        this.also(AstraLibs.instance::initialize)
        Logger.buildWithSpigot("AstraLibs", this).also(AstraLibs.logger::initialize)
        getCommand("test")?.setExecutor { sender, command, label, args ->
            val player = sender as Player
            KyoriComponentSerializer.Legacy.toComponent("&2Hello!").run(player::sendMessage)
            KyoriComponentSerializer.Legacy.toComponent("§2Hello!").run(player::sendMessage)
            KyoriComponentSerializer.Legacy.toComponent("&#42f5c5Hello!").run(player::sendMessage)
            KyoriComponentSerializer.Legacy.toComponent("#42f5c5Hello!").run(player::sendMessage)
            true
        }
    }

    override fun onDisable() {
        super.onDisable()
        pluginScope.close()
        inventoryClickEvent.onDisable()
    }
}
