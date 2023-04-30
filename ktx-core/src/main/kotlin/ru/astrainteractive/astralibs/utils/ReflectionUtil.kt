package ru.astrainteractive.astralibs.utils

import org.jetbrains.kotlin.tooling.core.UnsafeApi

/**
 * This class will help to use java reflection
 */
@UnsafeApi("Use on your own risk!")
object ReflectionUtil {
    @Suppress("UNCHECKED_CAST")
    fun <T, K> getDeclaredField(clazz: Class<T>, name: String) = kotlin.runCatching {
        clazz.getDeclaredField(name).run {
            isAccessible = true
            val field = this.get(null)
            isAccessible = false
            field as? K
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, K> getField(clazz: Class<T>, name: String) = kotlin.runCatching {
        clazz.getField(name).run {
            isAccessible = true
            val field = this.get(null)
            isAccessible = false
            field as? K
        }
    }

    fun <T, K> setDeclaredField(clazz: Class<T>, instance: Any, name: String, value: K?) = kotlin.runCatching {
        clazz.getDeclaredField(name).run {
            isAccessible = true
            set(instance, value)
            isAccessible = false
        }
    }
}
