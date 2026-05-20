package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.location.KLocation

/** Platform-agnostic abstraction for an entity that has a world position. */
fun interface Locatable {
    fun getLocation(): KLocation
}
