package ru.astrainteractive.astralibs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.menu.AstraMenuSize
import ru.astrainteractive.astralibs.menu.DefaultPlayerHolder
import ru.astrainteractive.astralibs.menu.IPlayerHolder
import ru.astrainteractive.astralibs.menu.Menu
import ru.astrainteractive.astralibs.menu.multi_page.MultiPageMenu
import ru.astrainteractive.astralibs.menu.multi_page.MultiPageViewModel
import ru.astrainteractive.astralibs.menu.one_page.OnePageMenu
import ru.astrainteractive.astralibs.menu.one_page.OnePageViewModel
import ru.astrainteractive.astralibs.utils.registerCommand

class PluginEntryPoint : JavaPlugin() {
    override fun onEnable() {
        super.onEnable()
        AstraLibs.rememberPlugin(this)
        Logger.prefix = "AstraLibsShowcase"
        AstraLibs.registerCommand("gui") { sender, args ->
            val player = (sender as? Player) ?: return@registerCommand
            PluginScope.launch(Dispatchers.IO) {
                if (args.isEmpty())
                    OnePageMenu(player).open()
                else MultiPageMenu(player).open()
            }

        }
    }

    override fun onDisable() {
        super.onDisable()
        PluginScope.cancel()
    }

}

