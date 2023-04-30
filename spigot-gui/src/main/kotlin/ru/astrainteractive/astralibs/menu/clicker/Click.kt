package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent

fun interface Click {
    operator fun invoke(e: InventoryClickEvent)
}
