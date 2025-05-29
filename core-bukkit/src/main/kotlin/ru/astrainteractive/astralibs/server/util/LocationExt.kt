package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.location.Location

fun Location.asBukkitLocation(): org.bukkit.Location {
    return org.bukkit.Location(
        Bukkit.getWorld(worldName),
        x,
        y,
        z,
    )
}

fun org.bukkit.Location.asAstraLocation(): Location {
    return Location(
        x = x,
        y = y,
        z = z,
        worldName = world.name
    )
}
