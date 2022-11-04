package ru.astrainteractive.astralibs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.commands.DSLCommand
import ru.astrainteractive.astralibs.menu.AstraMenuSize
import ru.astrainteractive.astralibs.menu.DefaultPlayerHolder
import ru.astrainteractive.astralibs.menu.IPlayerHolder
import ru.astrainteractive.astralibs.menu.Menu
import ru.astrainteractive.astralibs.menu.multi_page.MultiPageMenu
import ru.astrainteractive.astralibs.menu.multi_page.MultiPageViewModel
import ru.astrainteractive.astralibs.menu.one_page.OnePageMenu
import ru.astrainteractive.astralibs.menu.one_page.OnePageViewModel
import ru.astrainteractive.astralibs.utils.registerCommand

class EGIVE{


}
class PluginEntryPoint : JavaPlugin() {
    // egive <player> <item> [amount]
    fun cmd() = DSLCommand("egive") {
        argument(
            index = 0,
            parser = {it?.let(Bukkit::getPlayer)},
            onError = {
                sender.sendMessage("Player not exists: $it")
            },
            onResult = {
                val player = it.value
                argument(
                    1,
                    parser = { it?.let(Material::getMaterial)?.let(::ItemStack) },
                    onError = {
                        sender.sendMessage("ItemStack not exists: $it")
                    },
                    onResult = {
                        val item = it.value
                        argument(
                            2,
                            parser = { it?.toIntOrNull() ?: 0 },
                            onResult = {
                                val amount = it.value
                                sender.sendMessage("Player ${player.name} received ${amount} ${item.type.name}")
                                player.inventory.addItem(item.apply { this.amount = amount })
                            }
                        )
                    }
                )
            }
        )
    }

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
        cmd()
    }

    override fun onDisable() {
        super.onDisable()
        PluginScope.cancel()
    }

}

