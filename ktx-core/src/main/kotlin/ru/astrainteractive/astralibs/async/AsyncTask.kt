package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

/**
 * Add this interface to your class to use coroutines
 */
interface AsyncTask : CoroutineScope {
    override val coroutineContext: CoroutineContext
}
