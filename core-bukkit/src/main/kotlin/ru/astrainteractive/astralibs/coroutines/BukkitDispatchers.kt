package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import ru.astrainteractive.klibs.mikro.core.dispatchers.KotlinDispatchers

/**
 * Interface for Bukkit dispatchers which contains Main and Async dispatchers
 */
@Suppress("VariableNaming")
interface BukkitDispatchers : KotlinDispatchers {
    val BukkitMain: MainCoroutineDispatcher
    val BukkitAsync: CoroutineDispatcher
    override val Main: MainCoroutineDispatcher
        get() = BukkitMain
}
