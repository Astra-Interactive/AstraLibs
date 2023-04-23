package ru.astrainteractive.astralibs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.kotlin.tooling.core.UnsafeApi
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.commands.EmpireGive
import ru.astrainteractive.astralibs.commands.registerCommand
import ru.astrainteractive.astralibs.di.Singleton
import ru.astrainteractive.astralibs.filemanager.DefaultSpigotFileManager
import ru.astrainteractive.astralibs.filemanager.SpigotFileManager
import ru.astrainteractive.astralibs.menu.multi_page.MultiPageMenu
import ru.astrainteractive.astralibs.menu.one_page.OnePageMenu
import ru.astrainteractive.astralibs.logging.Logger
import ru.astrainteractive.astralibs.utils.setupWithSpigot

@OptIn(UnsafeApi::class)

class PluginEntryPoint : JavaPlugin() {
    companion object : Singleton<PluginEntryPoint>()

    override fun onEnable() {
        super.onEnable()
        PluginEntryPoint.instance = this
        Logger.setupWithSpigot("AstraLibsShowcase", this)
        registerCommand("gui") {
            val player = (sender as? Player) ?: return@registerCommand
            PluginScope.launch(Dispatchers.IO) {
                if (args.isEmpty())
                    OnePageMenu(this@PluginEntryPoint, player).open()
                else MultiPageMenu(this@PluginEntryPoint, player).open()
            }

        }
        EmpireGive.register()
        val spigotFileManager = DefaultSpigotFileManager(this, "config.yml")
        spigotFileManager.reload()
    }

    override fun onDisable() {
        super.onDisable()
        PluginScope.close()
    }

}

