package ru.astrainteractive.astralibs.server.location

import kotlinx.serialization.Serializable
import kotlin.math.pow
import kotlin.math.sqrt

@Serializable
data class Location(
    val x: Double,
    val y: Double,
    val z: Double,
    val worldName: String
)

fun Location.round(): Location {
    return copy(
        x = x.toInt().toDouble(),
        y = y.toInt().toDouble(),
        z = z.toInt().toDouble(),
    )
}

fun Location.dist(other: Location): Double {
    return sqrt((x - other.x).pow(2.0) + (y - other.y).pow(2.0) + (z - other.z).pow(2.0))
}
