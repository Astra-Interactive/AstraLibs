package ru.astrainteractive.astralibs.menu.event

import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.event.EventListener
import ru.astrainteractive.astralibs.menu.gui.Menu

/**
 * Bukkit event listener that routes inventory events to the active [Menu]. Register once in [JavaPlugin.onEnable].
 */
public interface DefaultInventoryClickEvent : EventListener

public fun DefaultInventoryClickEvent(): DefaultInventoryClickEvent = MenuInventoryEventListener()
