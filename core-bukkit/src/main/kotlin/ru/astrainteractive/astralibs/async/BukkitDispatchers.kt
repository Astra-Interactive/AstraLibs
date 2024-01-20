package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher
import ru.astrainteractive.klibs.mikro.core.dispatchers.KotlinDispatchers

/**
 * Interface for Bukkit dispatchers which contains Main and Async dispatchers
 */
@Suppress("VariableNaming")
interface BukkitDispatchers : KotlinDispatchers {
    val BukkitMain: CoroutineDispatcher
    val BukkitAsync: CoroutineDispatcher
    override val Main: CoroutineDispatcher
        get() = BukkitMain
}
