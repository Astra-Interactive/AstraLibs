package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.api.ObjectEncoder
import ru.astrainteractive.astralibs.encoding.factory.BukkitObjectStreamFactory

/** [ObjectEncoder] that uses Bukkit-aware streams,
 *  enabling serialization of types like [org.bukkit.inventory.ItemStack].
 * */
public class BukkitObjectEncoder : ObjectEncoder by ObjectEncoder.Default(BukkitObjectStreamFactory)
