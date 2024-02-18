package ru.astrainteractive.astralibs.encoding.factory

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * This factory will create [ObjectInputStream] or [ObjectOutputStream] depending on different implementation
 */
interface ObjectStreamFactory {
    fun createInputStream(istream: ByteArrayInputStream): ObjectInputStream
    fun createOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream
}
