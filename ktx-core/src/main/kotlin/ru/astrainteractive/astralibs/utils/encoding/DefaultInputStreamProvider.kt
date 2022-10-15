package ru.astrainteractive.astralibs.utils.encoding

import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

object DefaultInputStreamProvider : IInputStreamProvider {
    override fun provide(istream: ByteArrayInputStream): ObjectInputStream = ObjectInputStream(istream)
}