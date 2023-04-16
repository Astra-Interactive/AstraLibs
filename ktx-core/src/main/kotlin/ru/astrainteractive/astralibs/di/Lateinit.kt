package ru.astrainteractive.astralibs.di

import org.jetbrains.kotlin.gradle.utils.`is`
import ru.astrainteractive.astralibs.di.Dependency

class Lateinit<T : Any> : Dependency<T> {
    private lateinit var lateinitValue: T
    fun initialize(value: T) {
        if (::lateinitValue.isInitialized) throw IllegalStateException("Value already initialized")
        this.lateinitValue = value

    }

    override val value: T
        get() {
            if (!::lateinitValue.isInitialized) throw IllegalStateException("Value is not initialized yet")
            return lateinitValue
        }
}