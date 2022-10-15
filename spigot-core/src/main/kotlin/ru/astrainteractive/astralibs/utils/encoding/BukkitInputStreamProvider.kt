package ru.astrainteractive.astralibs.utils.encoding

import org.bukkit.util.io.BukkitObjectInputStream
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

object BukkitInputStreamProvider : IInputStreamProvider {
    override fun provide(istream: ByteArrayInputStream): ObjectInputStream = BukkitObjectInputStream(istream)
}