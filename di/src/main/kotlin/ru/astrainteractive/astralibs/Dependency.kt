package ru.astrainteractive.astralibs

import kotlin.reflect.KProperty

interface Dependency<out T> {
    val value: T
}

inline operator fun <reified T, K> Dependency<T>.getValue(t: K?, property: KProperty<*>): T = value
