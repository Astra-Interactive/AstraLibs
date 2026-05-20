package ru.astrainteractive.astralibs.menu.layout

import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/** DSL builder for [InventoryLayout]. All rows must have the same width. */
class InventoryLayoutBuilder<KEY, SLOT> {

    private val rows = mutableListOf<List<KEY>>()

    fun row(vararg keys: KEY) {
        rows += keys.toList()
    }

    /** Appends a row of [size] slots all bound to [key]. */
    fun row(size: Int, key: KEY) {
        rows += List(size) { key }
    }

    fun build(): InventoryLayout<KEY, SLOT> = DefaultInventoryLayout(rows)
}

fun <KEY, SLOT> inventoryLayout(
    block: InventoryLayoutBuilder<KEY, SLOT>.() -> Unit
): InventoryLayout<KEY, SLOT> = InventoryLayoutBuilder<KEY, SLOT>()
    .apply(block)
    .build()

/** Shorthand for [inventoryLayout] with [InventorySlot] as the slot type. */
fun <KEY> slotInventoryLayout(
    block: InventoryLayoutBuilder<KEY, InventorySlot>.() -> Unit
) = inventoryLayout(block)
