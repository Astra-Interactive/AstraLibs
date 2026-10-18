package ru.astrainteractive.astralibs.coroutines.util

import ru.astrainteractive.astralibs.coroutines.model.CoroutineTimings
import ru.astrainteractive.klibs.mikro.core.coroutines.CoroutineFeature

/** Adds a [CoroutineTimings] element to this scope so Spigot/Paper timings show the coroutine name. */
public fun CoroutineFeature.withTimings(): CoroutineFeature.Default {
    return CoroutineFeature.Default(this.coroutineContext + CoroutineTimings())
}
