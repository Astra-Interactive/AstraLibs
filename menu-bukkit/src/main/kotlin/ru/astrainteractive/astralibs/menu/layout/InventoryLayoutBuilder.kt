package ru.astrainteractive.astralibs.menu.layout

import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/**
 * DSL builder for [InventoryLayout].
 *
 * Layouts are described row-by-row; every row must have the same width or [build] will fail.
 * Prefer the [inventoryLayout] / [slotInventoryLayout] entrypoints over instantiating directly.
 */
class InventoryLayoutBuilder<KEY, SLOT> {

    private val rows = mutableListOf<List<KEY>>()

    /** Appends a row whose slots are described positionally by [keys]. */
    fun row(vararg keys: KEY) {
        rows += keys.toList()
    }

    /** Appends a row of [size] slots all bound to the same [key] (e.g. a row of borders). */
    fun row(size: Int, key: KEY) {
        rows += List(size) { key }
    }

    fun build(): InventoryLayout<KEY, SLOT> = DefaultInventoryLayout(rows)
}

/** Generic entrypoint for the [InventoryLayoutBuilder] DSL. */
fun <KEY, SLOT> inventoryLayout(
    block: InventoryLayoutBuilder<KEY, SLOT>.() -> Unit
): InventoryLayout<KEY, SLOT> = InventoryLayoutBuilder<KEY, SLOT>()
    .apply(block)
    .build()

/**
 * Shortcut for [inventoryLayout] producing [InventorySlot]s — the common case for Bukkit menus.
 */
fun <KEY> slotInventoryLayout(
    block: InventoryLayoutBuilder<KEY, InventorySlot>.() -> Unit
) = inventoryLayout(block)
