package ru.astrainteractive.astralibs.configuration


abstract class Configuration<T> {
    abstract var value: T
        protected set
    abstract val path: String
}