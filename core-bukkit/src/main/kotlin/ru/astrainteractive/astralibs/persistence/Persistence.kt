package ru.astrainteractive.astralibs.persistence

import org.bukkit.inventory.meta.ItemMeta

/** Extension helpers for reading and
 * writing [BukkitConstant]-typed values
 * on [ItemMeta]'s persistent data container. */
object Persistence {
    /** Writes [value] under the key defined by [b]; silently ignored when [value] is `null`. */
    fun <T, Z> ItemMeta.setPersistentDataType(b: BukkitConstant<T, Z>, value: Z) {
        persistentDataContainer.set(
            b.value,
            b.dataType,
            value ?: return
        )
    }

    /** Returns the value stored under [b]'s key, or `null` if absent. */
    fun <T : Any, Z : Any> ItemMeta.getPersistentDataOrNull(b: BukkitConstant<T, Z>): Z? {
        return this.persistentDataContainer.get(
            b.value,
            b.dataType
        )
    }

    /** Returns `true` if the persistent data container contains a value under [b]'s key. */
    fun <T : Any, Z : Any> ItemMeta.hasPersistentData(b: BukkitConstant<T, Z>): Boolean {
        return this.persistentDataContainer.has(
            b.value,
            b.dataType
        )
    }
}
