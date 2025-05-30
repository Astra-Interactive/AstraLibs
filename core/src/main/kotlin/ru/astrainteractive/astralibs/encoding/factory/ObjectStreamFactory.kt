package ru.astrainteractive.astralibs.encoding.factory

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * Factory interface for creating Java object streams.
 *
 * Used to abstract the creation of [ObjectInputStream] and [ObjectOutputStream],
 * allowing for pluggable implementations of object serialization mechanisms.
 */
interface ObjectStreamFactory {
    /**
     * Creates a new [ObjectInputStream] from the provided [ByteArrayInputStream].
     *
     * @param istream The input stream containing serialized object data.
     * @return A new [ObjectInputStream] instance for deserialization.
     */
    fun createInputStream(istream: ByteArrayInputStream): ObjectInputStream

    /**
     * Creates a new [ObjectOutputStream] from the provided [ByteArrayOutputStream].
     *
     * @param ostream The output stream where object data will be serialized.
     * @return A new [ObjectOutputStream] instance for serialization.
     */
    fun createOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream
}
