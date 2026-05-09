package ru.astrainteractive.astralibs.menu.inventory.model

/**
 * Standard chest-style inventory sizes in slots (multiples of 9, up to 6 rows).
 */
@Suppress("MagicNumber")
enum class InventorySize(val size: Int) {
    XXS(9),
    XS(18),
    S(27),
    M(36),
    L(45),
    XL(54)
}
