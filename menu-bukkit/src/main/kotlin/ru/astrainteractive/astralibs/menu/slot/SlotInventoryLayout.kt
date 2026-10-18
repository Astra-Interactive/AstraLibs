package ru.astrainteractive.astralibs.menu.slot

import ru.astrainteractive.astralibs.menu.layout.model.InventoryLayout
import ru.astrainteractive.astralibs.menu.layout.model.InventoryLayoutBuilder
import ru.astrainteractive.astralibs.menu.layout.model.inventoryLayout

/** Shorthand for [inventoryLayout] with [InventorySlot] as the slot type. */
public fun <KEY> slotInventoryLayout(
    block: InventoryLayoutBuilder<KEY, InventorySlot>.() -> Unit
): InventoryLayout<KEY, InventorySlot> = inventoryLayout(block)
