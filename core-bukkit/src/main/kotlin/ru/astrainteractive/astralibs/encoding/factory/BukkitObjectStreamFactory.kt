package ru.astrainteractive.astralibs.encoding.factory

import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * This custom [ObjectStreamFactory] is required to use with Bukkit objects
 */
object BukkitObjectStreamFactory : ObjectStreamFactory {
    override fun createInputStream(istream: ByteArrayInputStream): ObjectInputStream {
        return BukkitObjectInputStream(istream)
    }

    override fun createOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream {
        return BukkitObjectOutputStream(ostream)
    }
}
