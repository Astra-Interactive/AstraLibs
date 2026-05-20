package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import ru.astrainteractive.klibs.mikro.core.dispatchers.KotlinDispatchers

/** Extends [KotlinDispatchers] with Bukkit-specific coroutine dispatchers. [Main] is aliased to [BukkitMain]. */
@Suppress("VariableNaming")
interface BukkitDispatchers : KotlinDispatchers {
    /** Dispatcher that schedules work on the Bukkit server main thread. */
    val BukkitMain: MainCoroutineDispatcher

    /** Dispatcher that schedules work off the main thread. */
    val BukkitAsync: CoroutineDispatcher

    override val Main: MainCoroutineDispatcher
        get() = BukkitMain
}
