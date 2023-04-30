package ru.astrainteractive.astralibs

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * This class is implementing [Lazy] property but also allows to use var
 *
 * Only for internal usage and only for [Reloadable]
 */
internal class LazyMutable<T>(val initializer: () -> T) : ReadWriteProperty<Any?, T> {
    private object UNINITIALIZED_VALUE
    private var prop: Any? = UNINITIALIZED_VALUE

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if (prop == UNINITIALIZED_VALUE) {
            synchronized(this) {
                return if (prop == UNINITIALIZED_VALUE) initializer().also { prop = it } else prop as T
            }
        } else {
            prop as T
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        synchronized(this) {
            prop = value
        }
    }
}
