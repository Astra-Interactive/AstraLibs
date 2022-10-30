package ru.astrainteractive.astralibs.di

import kotlin.reflect.KProperty

/**
 * [IModule] could be used to create Singletons with only one initialization
 * If you want to use singleton which could be reloadable see [IReloadable]
 */
abstract class IModule<T> {
    protected abstract fun initializer(): T
    private val lazyValue by lazy {
        initializer()
    }
    val value: T
        get() = lazyValue
}
fun <T> module(initializer: () -> T) = object : IModule<T>() {
    override fun initializer(): T = initializer()
}
inline operator fun <reified T, K> IModule<T>.getValue(t: K?, property: KProperty<*>): T = value