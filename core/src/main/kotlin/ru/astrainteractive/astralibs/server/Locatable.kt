package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.server.location.Location

fun interface Locatable {
    fun getLocation(): Location

    interface Factory<T : Any> {
        fun from(instance: T): Locatable
    }
}
