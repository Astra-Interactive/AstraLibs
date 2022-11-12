package ru.astrainteractive.astralibs.di

import kotlin.reflect.KProperty

/**
 * [IValue] could be used to create custom getters
 */
abstract class IValue<T> {
    protected abstract fun initializer(): T
    val value: T
        get() = initializer()
}
fun <T> value(initializer: () -> T) = object : IValue<T>() {
    override fun initializer(): T = initializer()
}
inline operator fun <reified T, K> IValue<T>.getValue(t: K?, property: KProperty<*>): T = value