package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration

/**
 * [MutableConfiguration] or [Configuration] wrapper for bukkit [FileConfiguration]
 */
class BukkitConfiguration<T>(
    val fileConfiguration: FileConfiguration,
    val path: String,
    val default: T,
    private val load: BukkitConfiguration<T>.() -> T,
    private val save: BukkitConfiguration<T>.(T) -> Unit
) : MutableConfiguration<T> {
    override var value: T = loadValue()
        set(value) = saveValue(value).also {
            field = value
        }

    override fun saveValue(value: T) = save.invoke(this, value)

    override fun loadValue(): T = load.invoke(this)
}