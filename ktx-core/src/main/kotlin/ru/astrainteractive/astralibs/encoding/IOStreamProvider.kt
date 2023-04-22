package ru.astrainteractive.astralibs.encoding

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

interface IOStreamProvider {
    fun provideInputStream(istream: ByteArrayInputStream): ObjectInputStream
    fun provideOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream
}