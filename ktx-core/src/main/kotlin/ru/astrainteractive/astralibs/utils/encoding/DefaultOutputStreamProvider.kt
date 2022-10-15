package ru.astrainteractive.astralibs.utils.encoding

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

object DefaultOutputStreamProvider:IOutputStreamProvider{
    override fun provide(io: ByteArrayOutputStream): ObjectOutputStream = ObjectOutputStream(io)
}