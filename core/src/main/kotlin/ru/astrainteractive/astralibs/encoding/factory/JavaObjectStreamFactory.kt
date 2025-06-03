package ru.astrainteractive.astralibs.encoding.factory

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * A default implementation of [ObjectStreamFactory] that uses standard Java
 * serialization mechanisms.
 *
 * This factory provides [ObjectInputStream] and [ObjectOutputStream] instances
 * for reading from and writing to byte streams using Java's built-in serialization.
 */
object JavaObjectStreamFactory : ObjectStreamFactory {

    override fun createInputStream(istream: ByteArrayInputStream): ObjectInputStream =
        ObjectInputStream(istream)

    override fun createOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream =
        ObjectOutputStream(ostream)
}
