package ru.astrainteractive.astralibs.configuration

import kotlin.reflect.KProperty

abstract class MutableConfiguration<T> : Configuration<T>() {
    abstract override var value: T
        public set
}
operator fun <K, T> MutableConfiguration<T>.setValue(instance: K, property: KProperty<*>, value: T) {
    this.value = value
}
