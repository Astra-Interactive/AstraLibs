package ru.astrainteractive.astralibs.encoding.factory

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/** Factory for creating [ObjectInputStream] and [ObjectOutputStream] from byte array streams. */
interface ObjectStreamFactory {
    fun createInputStream(istream: ByteArrayInputStream): ObjectInputStream
    fun createOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream
}
