package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

/**
 * Add this interface to your class to use coroutines
 */
interface AsyncTask : CoroutineScope {
    private val job: Job
        get() = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}
