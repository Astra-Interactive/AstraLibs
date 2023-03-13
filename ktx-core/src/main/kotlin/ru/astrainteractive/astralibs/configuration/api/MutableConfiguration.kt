package ru.astrainteractive.astralibs.configuration.api

/**
 * [MutableConfiguration<T>] allows you to save/load values from your configuration file
 */
interface MutableConfiguration<T> : Configuration<T> {
    /**
     * Current state of a [value]
     */
    override val value: T

    /**
     * Load value from storage and update [value]
     */
    override fun loadValue(): T

    /**
     * Save new value into storage and update current
     */
    fun saveValue(value: T)

    /**
     * Save value with a refernce to current
     */
    fun saveValue(block: (T) -> T)

    /**
     * Reset value to to default
     */
    fun reset()

    /**
     * Convert to Immutable [Configuration]
     */
    fun toImmutable() = this as Configuration<T>
}

