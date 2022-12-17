package ru.astrainteractive.astralibs.di

import kotlin.reflect.KProperty

/**
 * IFactory is need for re-creating object, for example, viewModels
 */
abstract class IFactory<T> : IDependency<T> {
    abstract fun provide(): T
}
inline operator fun <reified T, K> IFactory<T>.getValue(t: K?, property: KProperty<*>): T = provide()
