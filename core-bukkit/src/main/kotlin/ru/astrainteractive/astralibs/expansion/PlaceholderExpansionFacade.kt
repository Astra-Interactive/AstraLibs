package ru.astrainteractive.astralibs.expansion

/**
 * This expansion is used to handle ClassNotFound exception
 */
interface PlaceholderExpansionFacade {
    /**
     * Check if this placeholder identifier has already been registered
     *
     * @return true if the identifier for this expansion is already registered
     */
    fun isRegistered(): Boolean

    /**
     * Attempt to register this PlaceholderExpansion
     *
     * @return true if this expansion is now registered with PlaceholderAPI
     */
    fun register(): Boolean

    /**
     * Attempt to unregister this PlaceholderExpansion
     *
     * @return true if this expansion is now unregistered with PlaceholderAPI
     */
    fun unregister(): Boolean
}
