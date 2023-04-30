package ru.astrainteractive.astralibs

import kotlin.reflect.KProperty

/**
 * [Reloadable] can be used to create reloadable components with kotlin object
 *
 * It's helpful in minecraft spigot plugins to read settings
 */
class Reloadable<T>(private val factory: Factory<T>) : Dependency<T> {

    override var value: T by LazyMutable {
        factory.build()
    }

    fun reload() {
        value = factory.build()
    }

}
