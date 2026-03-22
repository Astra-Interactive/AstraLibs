package ru.astrainteractive.astralibs.server.annotation

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is not intended to be subclassed outside the library"
)
annotation class InternalPlatformApi
