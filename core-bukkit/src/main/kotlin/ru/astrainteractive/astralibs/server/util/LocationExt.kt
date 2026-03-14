package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.location.KLocation

fun KLocation.asBukkitLocation(): org.bukkit.Location {
    return org.bukkit.Location(
        Bukkit.getWorld(worldName),
        x,
        y,
        z,
    )
}

fun org.bukkit.Location.asKLocation(): KLocation {
    return KLocation(
        x = x,
        y = y,
        z = z,
        worldName = world.name
    )
}
