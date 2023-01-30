package ru.astrainteractive.astralibs.di

import kotlin.reflect.KProperty

/**
 * [Factory] could be used to create custom getters
 */
abstract class Factory<T> : Dependency<T> {
    protected abstract fun initializer(): T
    override val value: T
        get() = initializer()
}

fun <T> factory(initializer: () -> T) = object : Factory<T>() {
    override fun initializer(): T = initializer()
}

inline operator fun <reified T, K> Factory<T>.getValue(t: K?, property: KProperty<*>): T = value