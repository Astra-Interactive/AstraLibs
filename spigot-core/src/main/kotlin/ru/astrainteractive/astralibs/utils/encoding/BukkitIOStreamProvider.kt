package ru.astrainteractive.astralibs.utils.encoding

import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object BukkitIOStreamProvider : IOStreamProvider {
    override fun provideInputStream(istream: ByteArrayInputStream): ObjectInputStream = BukkitObjectInputStream(istream)

    override fun provideOutputStream(ostream: ByteArrayOutputStream): ObjectOutputStream = BukkitObjectOutputStream(ostream)
}