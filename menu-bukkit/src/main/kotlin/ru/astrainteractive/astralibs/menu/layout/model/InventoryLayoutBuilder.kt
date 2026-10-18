package ru.astrainteractive.astralibs.menu.layout.model

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
