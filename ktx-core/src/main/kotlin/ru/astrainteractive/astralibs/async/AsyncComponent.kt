package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

abstract class AsyncComponent: Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO
    val componentScope: CoroutineScope
        get() = this

    override fun close() {
        coroutineContext.cancel()
        this.cancel()
    }


}