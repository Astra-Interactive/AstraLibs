package ru.astrainteractive.astralibs.expansion

/** Facade for PlaceholderAPI expansion registration, used to guard against ClassNotFound at runtime. */
public interface PlaceholderExpansionFacade {
    public fun isRegistered(): Boolean
    public fun register(): Boolean
    public fun unregister(): Boolean
}
