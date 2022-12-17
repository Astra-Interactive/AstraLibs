package ru.astrainteractive.astralibs.di

import kotlin.reflect.KProperty

/**
 * [IReloadable] can be used to create reloadable singletons with kotlin object
 * If you want to create non-reloadable singleton - see [IModule]
 */
abstract class IReloadable<T>: IDependency<T> {
    abstract fun initializer(): T

    final override var value: T by LazyMutable {
        initializer()
    }

    fun reload() {
        value = initializer()
    }

}
fun <T> reloadable(initializer: () -> T) = object : IReloadable<T>() {
    override fun initializer(): T = initializer()
}
inline operator fun <reified T, K> IReloadable<T>.getValue(t: K?, property: KProperty<*>): T = value