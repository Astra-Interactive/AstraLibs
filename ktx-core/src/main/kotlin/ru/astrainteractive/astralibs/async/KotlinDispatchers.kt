package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher

@Suppress("VariableNaming")
interface KotlinDispatchers {
    val IO: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
}
