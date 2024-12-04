package ru.astrainteractive.astralibs.persistence

import org.bukkit.inventory.meta.ItemMeta

object Persistence {
    fun <T, Z> ItemMeta.setPersistentDataType(b: BukkitConstant<T, Z>, value: Z) {
        persistentDataContainer.set(
            b.value,
            b.dataType,
            value ?: return
        )
    }

    fun <T : Any, Z : Any> ItemMeta.getPersistentDataOrNull(b: BukkitConstant<T, Z>): Z? {
        return this.persistentDataContainer.get(
            b.value,
            b.dataType
        )
    }

    fun <T : Any, Z : Any> ItemMeta.hasPersistentData(b: BukkitConstant<T, Z>): Boolean {
        return this.persistentDataContainer.has(
            b.value,
            b.dataType
        )
    }
}
