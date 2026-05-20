package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import ru.astrainteractive.klibs.mikro.core.dispatchers.KotlinDispatchers

/**
 * [KotlinDispatchers] for vanilla Minecraft servers.
 *
 * [Main] schedules on the server main thread via [MinecraftMainDispatcher];
 * [IO], [Default], and [Unconfined] delegate to [kotlinx.coroutines.Dispatchers].
 */
class MinecraftDispatchers : KotlinDispatchers {
    override val Main: MainCoroutineDispatcher by lazy {
        MinecraftMainDispatcher()
    }
    override val IO: CoroutineDispatcher
        get() = Dispatchers.IO
    override val Default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val Unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
