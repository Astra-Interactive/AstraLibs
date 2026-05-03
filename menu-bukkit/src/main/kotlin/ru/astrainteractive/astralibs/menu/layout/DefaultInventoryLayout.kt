package ru.astrainteractive.astralibs.menu.layout

/**
 * Default [InventoryLayout] backed by a flat list of [KEY]s, one per slot.
 *
 * Construct via the [DefaultInventoryLayout] factory taking rows, or through the
 * [inventoryLayout] / [slotInventoryLayout] DSL.
 */
class DefaultInventoryLayout<KEY, SLOT>(
    private val slots: List<KEY>
) : InventoryLayout<KEY, SLOT> {

    override val size: Int get() = slots.size

    override fun keyAt(index: Int): KEY = slots[index]

    override fun indicesOf(key: KEY): List<Int> = slots.withIndex()
        .filter { indexedValue -> indexedValue.value == key }
        .map { indexedValue -> indexedValue.index }

    override fun firstIndexOf(key: KEY): Int = slots.indexOf(key)
        .takeIf { index -> index != -1 }
        ?: error("Key $key not found in layout")

    override fun count(key: KEY): Int = slots.count { slotKey -> slotKey == key }

    override fun mapSlotsNotNull(
        key: KEY,
        transform: (index: Int) -> SLOT?
    ): List<SLOT> = indicesOf(key).mapNotNull(transform)
}

/**
 * Builds a [DefaultInventoryLayout] from a list of equally-sized rows
 *
 * All rows must share the same width
 * An empty [rows] list yields an empty layout
 *
 * @throws IllegalArgumentException if rows are not equally sized
 */
@Suppress("FunctionNaming")
fun <KEY, SLOT> DefaultInventoryLayout(rows: List<List<KEY>>): InventoryLayout<KEY, SLOT> {
    if (rows.isEmpty()) return DefaultInventoryLayout(slots = emptyList())

    val width = rows.first().size
    require(rows.all { row -> row.size == width }) {
        "All rows must have the same width"
    }

    return DefaultInventoryLayout(slots = rows.flatten())
}
