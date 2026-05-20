package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.location.KLocation

/** Platform-agnostic abstraction for an entity that can be teleported to a world position. */
fun interface Teleportable {
    fun teleport(kLocation: KLocation)
}
