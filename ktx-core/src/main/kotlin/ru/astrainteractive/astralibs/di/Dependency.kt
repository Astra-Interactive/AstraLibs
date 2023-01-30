package ru.astrainteractive.astralibs.di

import kotlin.reflect.KProperty

interface Dependency<T> {
    val value: T
}

inline operator fun <reified T, K> Dependency<T>.getValue(t: K?, property: KProperty<*>): T = value