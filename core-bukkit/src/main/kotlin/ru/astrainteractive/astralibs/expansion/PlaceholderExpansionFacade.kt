package ru.astrainteractive.astralibs.expansion

/** Facade for PlaceholderAPI expansion registration, used to guard against ClassNotFound at runtime. */
interface PlaceholderExpansionFacade {
    fun isRegistered(): Boolean
    fun register(): Boolean
    fun unregister(): Boolean
}
