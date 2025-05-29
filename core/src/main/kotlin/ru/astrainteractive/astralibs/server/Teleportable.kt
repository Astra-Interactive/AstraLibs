package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.location.Location

fun interface Teleportable {
    fun teleport(location: Location)

    interface Factory<T : Any> {
        fun from(instance: T): Teleportable
    }
}
