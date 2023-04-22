package ru.astrainteractive.astralibs.di

import kotlin.reflect.KProperty

/**
 * [Singleton] is used for lazy initialization for classes which can't be created as kotlin objects
 * For example: Spigot plugin - anyway you will be use instance for it so [Singleton] can be useful here
 */
abstract class Singleton<T : Any>: Dependency<T> {
    lateinit var instance: T
    override val value: T
        get() = instance
}

inline operator fun <reified T: Any, K> Singleton<T>.getValue(t: K?, property: KProperty<*>): T = instance