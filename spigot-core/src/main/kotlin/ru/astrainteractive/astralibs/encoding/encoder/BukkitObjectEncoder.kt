package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.factory.BukkitObjectStreamFactory

/**
 * Default implementation for Bukkit objects
 */
class BukkitObjectEncoder : ObjectEncoder by DefaultObjectEncoder(BukkitObjectStreamFactory)
