package ru.astrainteractive.astralibs.configuration

fun <T> configuration(
    path: String,
    init: () -> T
): Configuration<T> {
    return object : Configuration<T>() {
        override var value: T = init()
        override val path: String = path
    }
}

fun <T> mutableConfiguration(
    path: String,
    init: () -> T,
    onUpdate: (T) -> Unit
): MutableConfiguration<T> {
    return object : MutableConfiguration<T>() {
        override var value: T = init()
            set(value) {
                field = value
                onUpdate(value)
            }
        override val path: String = path
    }
}

