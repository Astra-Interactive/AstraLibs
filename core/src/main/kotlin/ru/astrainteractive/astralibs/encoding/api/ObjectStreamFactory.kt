package ru.astrainteractive.astralibs.encoding.api

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/** Factory for creating [ObjectInputStream] and [ObjectOutputStream] from byte array streams. */
public interface ObjectStreamFactory {
    public fun createInputStream(istream: ByteArrayInputStream): ObjectInputStream

    public fun createOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream

    /** [ObjectStreamFactory] using standard Java serialization. */
    public object Java : ObjectStreamFactory {
        override fun createInputStream(istream: ByteArrayInputStream): ObjectInputStream =
            ObjectInputStream(istream)

        override fun createOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream =
            ObjectOutputStream(ostream)
    }
}
