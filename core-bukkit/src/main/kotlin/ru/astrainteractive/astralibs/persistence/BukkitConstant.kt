package ru.astrainteractive.astralibs.persistence

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

interface BukkitConstant<T, Z> {
    val value: NamespacedKey
    val dataType: PersistentDataType<T, Z>

    class Default<T, Z>(
        override val value: NamespacedKey,
        override val dataType: PersistentDataType<T, Z>
    ) : BukkitConstant<T, Z>
}

@Suppress("FunctionNaming")
fun <T, Z> BukkitConstant(plugin: Plugin, key: String, dataType: PersistentDataType<T, Z>): BukkitConstant<T, Z> {
    return BukkitConstant.Default<T, Z>(
        value = NamespacedKey(plugin, key),
        dataType = dataType
    )
}

@Suppress("FunctionNaming")
fun <T, Z> BukkitConstant(namespace: String, key: String, dataType: PersistentDataType<T, Z>): BukkitConstant<T, Z> {
    return BukkitConstant.Default<T, Z>(
        value = NamespacedKey(namespace, key),
        dataType = dataType
    )
}
