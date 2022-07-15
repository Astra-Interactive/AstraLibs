package com.astrainteractive.astralibs.utils

object ReflectionUtil {
    @Suppress("UNCHECKED_CAST")
    fun <T, K> getDeclaredField(clazz: Class<T>, name: String): K? = catching(true) {
        clazz.getDeclaredField(name).run {
            isAccessible = true
            val field = this.get(null)
            isAccessible = false
            field as? K?
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, K> getField(clazz: Class<T>, name: String): K? = catching(true) {
        clazz.getField(name).run {
            isAccessible = true
            val field = this.get(null)
            isAccessible = false
            field as? K?
        }
    }

    fun <T, K> setDeclaredField(clazz: Class<T>, instance: Any, name: String, value: K?) = catching(true) {
        clazz.getDeclaredField(name).run {
            isAccessible = true
            set(instance, value)
            isAccessible = false
        }

    }
}