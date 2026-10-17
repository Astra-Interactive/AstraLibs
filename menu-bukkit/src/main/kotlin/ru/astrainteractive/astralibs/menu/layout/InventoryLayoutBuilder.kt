package ru.astrainteractive.astralibs.menu.layout

import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/** DSL builder for [InventoryLayout]. All rows must have the same width. */
public class InventoryLayoutBuilder<KEY, SLOT> {

    private val rows = mutableListOf<List<KEY>>()

    public fun row(vararg keys: KEY) {
        rows += keys.toList()
    }

    /** Appends a row of [size] slots all bound to [key]. */
    public fun row(size: Int, key: KEY) {
        rows += List(size) { key }
    }

    public fun build(): InventoryLayout<KEY, SLOT> = DefaultInventoryLayout(rows)
}

public fun <KEY, SLOT> inventoryLayout(
    block: InventoryLayoutBuilder<KEY, SLOT>.() -> Unit
): InventoryLayout<KEY, SLOT> = InventoryLayoutBuilder<KEY, SLOT>()
    .apply(block)
    .build()

/** Shorthand for [inventoryLayout] with [InventorySlot] as the slot type. */
public fun <KEY> slotInventoryLayout(
    block: InventoryLayoutBuilder<KEY, InventorySlot>.() -> Unit
): InventoryLayout<KEY, InventorySlot> = inventoryLayout(block)
