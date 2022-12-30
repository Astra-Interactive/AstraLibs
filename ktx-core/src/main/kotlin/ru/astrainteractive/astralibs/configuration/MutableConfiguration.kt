package ru.astrainteractive.astralibs.configuration

abstract class MutableConfiguration<T> : Configuration<T>() {
    abstract override var value: T
        public set
}