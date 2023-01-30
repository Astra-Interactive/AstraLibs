package ru.astrainteractive.astralibs.configuration

interface Configuration<T> {
    abstract fun loadValue(): T
    abstract val value: T
}