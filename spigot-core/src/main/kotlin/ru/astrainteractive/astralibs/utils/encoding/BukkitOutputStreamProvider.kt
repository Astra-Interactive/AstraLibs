package ru.astrainteractive.astralibs.utils.encoding

import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

object BukkitOutputStreamProvider:IOutputStreamProvider{
    override fun provide(io: ByteArrayOutputStream): ObjectOutputStream = BukkitObjectOutputStream(io)
}