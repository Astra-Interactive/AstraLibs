package ru.astrainteractive.astralibs.server.api

import ru.astrainteractive.astralibs.server.location.model.KLocation

/** Platform-agnostic abstraction for an entity that has a world position. */
public fun interface Locatable {
    public fun getLocation(): KLocation
}
