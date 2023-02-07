package ru.astrainteractive.astralibs.menu.utils.click

import org.bukkit.event.inventory.InventoryClickEvent

fun interface Click {
    operator fun invoke(e: InventoryClickEvent)
}