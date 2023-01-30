package ru.astrainteractive.astralibs.di

import kotlin.reflect.KProperty

/**
 * [Reloadable] can be used to create reloadable singletons with kotlin object
 * If you want to create non-reloadable singleton - see [Module]
 */
abstract class Reloadable<T>: Dependency<T> {
    abstract fun initializer(): T

    final override var value: T by LazyMutable {
        initializer()
    }

    fun reload() {
        value = initializer()
    }

}
fun <T> reloadable(initializer: () -> T) = object : Reloadable<T>() {
    override fun initializer(): T = initializer()
}
inline operator fun <reified T, K> Reloadable<T>.getValue(t: K?, property: KProperty<*>): T = value