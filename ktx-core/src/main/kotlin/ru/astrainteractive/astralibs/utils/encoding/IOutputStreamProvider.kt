package ru.astrainteractive.astralibs.utils.encoding

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

interface IOutputStreamProvider {
    fun provide(io: ByteArrayOutputStream): ObjectOutputStream
}