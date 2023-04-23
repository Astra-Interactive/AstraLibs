package ru.astrainteractive.astralibs.persistence

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

class BukkitConstant<T, Z>(
    val plugin: Plugin,
    val value: NamespacedKey,
    val dataType: PersistentDataType<T, Z>
) {
    constructor(
        plugin: Plugin,
        key: String, dataType: PersistentDataType<T, Z>
    ) : this(
        plugin = plugin,
        value = NamespacedKey(plugin, key),
        dataType = dataType
    )
}