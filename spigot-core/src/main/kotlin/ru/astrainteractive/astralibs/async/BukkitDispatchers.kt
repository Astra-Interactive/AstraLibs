package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Interface for Bukkit dispatchers which contains Main and Async dispatchers
 */
interface BukkitDispatchers : KotlinDispatchers {
    val BukkitMain: CoroutineDispatcher
    val BukkitAsync: CoroutineDispatcher
}