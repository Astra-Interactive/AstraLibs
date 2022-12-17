package ru.astrainteractive.astralibs.di

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface IDependency<T> {
    val value: T
}

inline operator fun <reified T, K> IDependency<T>.getValue(t: K?, property: KProperty<*>): T = value