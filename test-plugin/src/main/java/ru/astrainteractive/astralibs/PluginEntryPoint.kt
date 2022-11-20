package ru.astrainteractive.astralibs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.commands.Command
import ru.astrainteractive.astralibs.commands.DSLCommand
import ru.astrainteractive.astralibs.commands.EmpireGive
import ru.astrainteractive.astralibs.menu.multi_page.MultiPageMenu
import ru.astrainteractive.astralibs.menu.one_page.OnePageMenu
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
        EmpireGive.register()
    }

    override fun onDisable() {
        super.onDisable()
        PluginScope.cancel()
    }

}

