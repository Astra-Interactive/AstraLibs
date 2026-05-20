package ru.astrainteractive.astralibs.server.annotation

/**
 * Marks a declaration as an internal platform API that must not be subclassed outside the library.
 *
 * Opting in to this annotation on external subclasses is an error, ensuring that platform-specific
 * implementations remain controlled within AstraLibs.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is not intended to be subclassed outside the library"
)
annotation class InternalPlatformApi
