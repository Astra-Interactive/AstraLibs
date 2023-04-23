package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

interface KDispatchers {
    val IO: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
}