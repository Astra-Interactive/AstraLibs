package ru.astrainteractive.astralibs.configuration.api

/**
 * [Configuration<T>] allows you to load values from your configuration file
 */
interface Configuration<T> {
    /**
     * Current state of a [value]
     */
    val value: T

    /**
     * Load value from storage and update [value]
     */
    fun loadValue(): T
}
