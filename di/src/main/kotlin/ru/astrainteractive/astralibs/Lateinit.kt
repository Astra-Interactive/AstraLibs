package ru.astrainteractive.astralibs

/**
 * [Lateinit] is used for components which can't be initialized internally
 * For example: Velocity @inject properties, Android context or spigot plugin instance
 *
 * It can't be initialized twice and can't be accessed until initialization
 */
class Lateinit<T : Any> : Dependency<T> {
    private lateinit var instance: T

    fun initialize(value: T) {
        if (::instance.isInitialized) throw IllegalStateException("Value already initialized")
        this.instance = value
    }

    fun initialize(factory: Factory<T>) {
        val value = factory.build()
        initialize(value)
    }

    override val value: T
        get() {
            if (!::instance.isInitialized) throw IllegalStateException("Value is not initialized yet")
            return instance
        }
}