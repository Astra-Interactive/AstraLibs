package ru.astrainteractive.astralibs.menu.inventory.model

/**
 * Standard chest-style inventory sizes in slots (multiples of 9, up to 6 rows).
 */
@Suppress("MagicNumber")
enum class InventorySize(val size: Int) {
    /** 9 slots — 1 row. */
    XXS(9),

    /** 18 slots — 2 rows. */
    XS(18),

    /** 27 slots — 3 rows. */
    S(27),

    /** 36 slots — 4 rows. */
    M(36),

    /** 45 slots — 5 rows. */
    L(45),

    /** 54 slots — 6 rows. */
    XL(54)
}
