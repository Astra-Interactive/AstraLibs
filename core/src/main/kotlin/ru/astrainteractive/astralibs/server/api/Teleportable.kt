package ru.astrainteractive.astralibs.server.api

import ru.astrainteractive.astralibs.server.location.model.KLocation

/** Platform-agnostic abstraction for an entity that can be teleported to a world position. */
public fun interface Teleportable {
    public fun teleport(kLocation: KLocation)
}
