package ru.astrainteractive.astralibs.menu.layout

/**
 * Read-only mapping between inventory slot indices and semantic [KEY]s.
 *
 * Each inventory slot index (0 until [size]) is associated with a [KEY] describing its purpose
 * (e.g. auction item, border, navigation button). [SLOT] is the concrete slot type produced when
 * binding keys to UI elements (typically `InventorySlot`).
 *
 * Build instances via [inventoryLayout] / [slotInventoryLayout] DSL.
 */
interface InventoryLayout<KEY, SLOT> {
    /** Total number of slots in the inventory (rows * width). */
    val size: Int

    /** Returns the [KEY] assigned to the given slot [index]. */
    fun keyAt(index: Int): KEY

    /** All slot indices that are assigned to [key]. */
    fun indicesOf(key: KEY): List<Int>

    /** First slot index assigned to [key]. Throws if [key] is not present. */
    fun firstIndexOf(key: KEY): Int

    /** Number of slots assigned to [key]. */
    fun count(key: KEY): Int

    /**
     * Maps every slot index of [key] through [transform], discarding nulls.
     *
     * Useful for materializing UI slots only where the layout actually places a [key].
     */
    fun mapSlotsNotNull(
        key: KEY,
        transform: (index: Int) -> SLOT?
    ): List<SLOT>
}
