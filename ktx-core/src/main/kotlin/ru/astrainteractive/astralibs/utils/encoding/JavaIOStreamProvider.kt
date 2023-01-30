package ru.astrainteractive.astralibs.utils.encoding

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object JavaIOStreamProvider: IOStreamProvider {
    override fun provideInputStream(istream: ByteArrayInputStream): ObjectInputStream = ObjectInputStream(istream)

    override fun provideOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream = ObjectOutputStream(ostream)
}