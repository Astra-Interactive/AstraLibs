package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.location.KLocation

fun interface Locatable {
    fun getLocation(): KLocation

    interface Factory<T : Any> {
        fun from(instance: T): Locatable
    }
}
