package ru.astrainteractive.astralibs.util

/** The value a [krateOf] krate last read from or saved into its file; `T` may itself be nullable. */
@PublishedApi
internal class KrateLastValue<T> {
    @Volatile
    private var isKnown = false

    @Volatile
    private var value: T? = null

    fun remember(value: T) {
        this.value = value
        isKnown = true
    }

    fun forget() {
        value = null
        isKnown = false
    }

    @Suppress("UNCHECKED_CAST")
    fun orElse(default: () -> T): T = if (isKnown) value as T else default.invoke()
}
