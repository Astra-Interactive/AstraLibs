package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import ru.astrainteractive.klibs.mikro.core.dispatchers.KotlinDispatchers

class NeoForgeDispatchers : KotlinDispatchers {
    override val Main: MainCoroutineDispatcher by lazy {
        NeoForgeMainDispatcher()
    }
    override val IO: CoroutineDispatcher
        get() = Dispatchers.IO
    override val Default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val Unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
