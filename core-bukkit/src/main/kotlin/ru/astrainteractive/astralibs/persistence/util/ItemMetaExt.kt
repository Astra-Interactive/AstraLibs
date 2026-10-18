package ru.astrainteractive.astralibs.persistence.util

import org.bukkit.inventory.meta.ItemMeta
import ru.astrainteractive.astralibs.persistence.api.BukkitConstant

/** Writes [value] under the key defined by [b]; silently ignored when [value] is `null`. */
public fun <T, Z> ItemMeta.setPersistentDataType(b: BukkitConstant<T, Z>, value: Z) {
    persistentDataContainer.set(
        b.value,
        b.dataType,
        value ?: return
    )
}

/** Returns the value stored under [b]'s key, or `null` if absent. */
public fun <T : Any, Z : Any> ItemMeta.getPersistentDataOrNull(b: BukkitConstant<T, Z>): Z? {
    return this.persistentDataContainer.get(
        b.value,
        b.dataType
    )
}

/** Returns `true` if the persistent data container contains a value under [b]'s key. */
public fun <T : Any, Z : Any> ItemMeta.hasPersistentData(b: BukkitConstant<T, Z>): Boolean {
    return this.persistentDataContainer.has(
        b.value,
        b.dataType
    )
}
