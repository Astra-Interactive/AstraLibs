package ru.astrainteractive.astralibs.menu.layout

/**
 * Read-only mapping between inventory slot indices and semantic [KEY]s.
 *
 * Build instances via [inventoryLayout] / [slotInventoryLayout] DSL.
 */
public interface InventoryLayout<KEY, SLOT> {
    public val size: Int

    public fun keyAt(index: Int): KEY

    public fun indicesOf(key: KEY): List<Int>

    /** @throws NoSuchElementException if [key] is not present. */
    public fun firstIndexOf(key: KEY): Int

    public fun count(key: KEY): Int

    public fun mapSlotsNotNull(
        key: KEY,
        transform: (index: Int) -> SLOT?
    ): List<SLOT>
}

/**
 * Like [InventoryLayout.mapSlotsNotNull] but also passes a zero-based iteration index
 * alongside the absolute slot index.
 */
public fun <KEY, SLOT> InventoryLayout<KEY, SLOT>.mapSlotsNotNullIndexed(
    key: KEY,
    transform: (iterationIndex: Int, slotIndex: Int) -> SLOT?
): List<SLOT> {
    var iterationIndex = 0
    return mapSlotsNotNull(key) { slotIndex ->
        val current = iterationIndex
        iterationIndex++
        transform(current, slotIndex)
    }
}
