package ru.astrainteractive.astralibs.configuration

import kotlin.reflect.KProperty


abstract class Configuration<T> {
    abstract var value: T
        protected set
    abstract val path: String
}
operator fun <K, T> Configuration<T>.getValue(instance: K, property: KProperty<*>): T {
    return this.value
}