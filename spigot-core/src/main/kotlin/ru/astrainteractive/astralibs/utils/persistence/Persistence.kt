package ru.astrainteractive.astralibs.utils.persistence

import org.bukkit.inventory.meta.ItemMeta

object Persistence {
    fun <T, Z> ItemMeta.setPersistentDataType(b: BukkitConstant<T, Z>, value: Z) {
        persistentDataContainer.set(
            b.value,
            b.dataType,
            value ?: return
        )
    }

    fun <T, Z> ItemMeta.getPersistentData(b: BukkitConstant<T, Z>) =
        this.persistentDataContainer.get(
            b.value,
            b.dataType
        )

    fun <T, Z> ItemMeta.hasPersistentData(b: BukkitConstant<T, Z>) =
        this.persistentDataContainer.has(
            b.value,
            b.dataType
        )

}

