package ru.astrainteractive.astralibs.persistence

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

/**
 * Pairs a [NamespacedKey] with its [PersistentDataType]
 * for use with a [org.bukkit.persistence.PersistentDataContainer].
 *
 * @param T The primitive storage type.
 * @param Z The complex type exposed to the caller.
 */
interface BukkitConstant<T, Z> {
    val value: NamespacedKey
    val dataType: PersistentDataType<T, Z>

    data class Default<T, Z>(
        override val value: NamespacedKey,
        override val dataType: PersistentDataType<T, Z>
    ) : BukkitConstant<T, Z>
}

/** Creates a [BukkitConstant] namespaced under [plugin]'s name. */
@Suppress("FunctionNaming")
fun <T, Z> BukkitConstant(plugin: Plugin, key: String, dataType: PersistentDataType<T, Z>): BukkitConstant<T, Z> {
    return BukkitConstant.Default<T, Z>(
        value = NamespacedKey(plugin, key),
        dataType = dataType
    )
}

/** Creates a [BukkitConstant] with an explicit [namespace]; prefer the [Plugin] overload when possible. */
@Suppress("FunctionNaming")
fun <T, Z> BukkitConstant(namespace: String, key: String, dataType: PersistentDataType<T, Z>): BukkitConstant<T, Z> {
    return BukkitConstant.Default<T, Z>(
        value = NamespacedKey(namespace, key),
        dataType = dataType
    )
}
