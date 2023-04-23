package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object DefaultKDispatchers: KDispatchers {
    override val IO: CoroutineDispatcher = Dispatchers.IO
    override val Default: CoroutineDispatcher = Dispatchers.Default
    override val Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}