package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.location.KLocation

/**
 * Converts this [KLocation] to a Bukkit [org.bukkit.Location].
 *
 * Yaw and pitch default to `0`. The world is `null` if [KLocation.worldName] does not match a loaded world.
 *
 * @see asKLocation
 */
fun KLocation.asBukkitLocation(): org.bukkit.Location {
    return org.bukkit.Location(
        Bukkit.getWorld(worldName),
        x,
        y,
        z,
    )
}

/**
 * Converts this Bukkit [org.bukkit.Location] to a [KLocation] snapshot. Yaw and pitch are not preserved.
 *
 * @see asBukkitLocation
 */
fun org.bukkit.Location.asKLocation(): KLocation {
    return KLocation(
        x = x,
        y = y,
        z = z,
        worldName = world.name
    )
}
