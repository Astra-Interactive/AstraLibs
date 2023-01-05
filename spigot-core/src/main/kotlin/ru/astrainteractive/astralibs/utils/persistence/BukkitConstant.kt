package ru.astrainteractive.astralibs.utils.persistence

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import ru.astrainteractive.astralibs.AstraLibs

class BukkitConstant<T, Z>(
    val value: NamespacedKey,
    val dataType: PersistentDataType<T, Z>
) {
    constructor(key: String, dataType: PersistentDataType<T, Z>) : this(
        NamespacedKey(AstraLibs.instance, key),
        dataType
    )
}