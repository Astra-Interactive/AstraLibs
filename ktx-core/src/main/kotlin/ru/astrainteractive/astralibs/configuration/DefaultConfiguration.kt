package ru.astrainteractive.astralibs.configuration

import ru.astrainteractive.astralibs.configuration.api.MutableConfiguration

/**
 * DefaultConfiguration which can help to implement custom types configurations via delegation
 */
class DefaultConfiguration<T>(
    val default: T,
    private val load: MutableConfiguration<T>.() -> T,
    private val save: MutableConfiguration<T>.(T) -> Unit
) : MutableConfiguration<T> {
    override var value: T = loadValue()
        set(value) = saveValue(value).also {
            field = value
        }

    override fun saveValue(value: T) = save.invoke(this, value)
    override fun saveValue(block: (T) -> T) = saveValue(block.invoke(value))

    override fun loadValue(): T = load.invoke(this)
    override fun reset() {
        saveValue(default)
    }
}
