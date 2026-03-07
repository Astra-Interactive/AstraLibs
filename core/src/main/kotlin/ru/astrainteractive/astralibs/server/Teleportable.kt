package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.location.KLocation

fun interface Teleportable {
    fun teleport(KLocation: KLocation)

    interface Factory<T : Any> {
        fun from(instance: T): Teleportable
    }
}
