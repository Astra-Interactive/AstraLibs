package ru.astrainteractive.astralibs.configuration

interface MutableConfiguration<T> : Configuration<T> {
    override var value: T
    fun saveValue(value: T)
    fun immutable() = this as Configuration<T>
}

