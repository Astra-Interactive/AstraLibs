package ru.astrainteractive.astralibs.encoding.factory

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/** [ObjectStreamFactory] using standard Java serialization. */
public object JavaObjectStreamFactory : ObjectStreamFactory {

    override fun createInputStream(istream: ByteArrayInputStream): ObjectInputStream =
        ObjectInputStream(istream)

    override fun createOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream =
        ObjectOutputStream(ostream)
}
