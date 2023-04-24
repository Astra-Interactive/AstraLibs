package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher

interface KotlinDispatchers {
    val IO: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
}