package ru.astrainteractive.astralibs.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

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

    fun <T> serializeItem(
        obj: T,
        objectOutputStreamCreator: (ByteArrayOutputStream) -> ObjectOutputStream = { ObjectOutputStream(it) }
    ): ByteArray {
        val io = ByteArrayOutputStream()
        val os = objectOutputStreamCreator(io)
        os.writeObject(obj)
        os.flush()
        return io.toByteArray()
    }

    fun <T> deserializeItem(
        byteArray: ByteArray,
        objectInputStreamCreator: (ByteArrayInputStream) -> ObjectInputStream = { ObjectInputStream(it) }
    ): T {
        val _in = ByteArrayInputStream(byteArray)
        val _is = objectInputStreamCreator(_in)
        return _is.readObject() as T
    }
}