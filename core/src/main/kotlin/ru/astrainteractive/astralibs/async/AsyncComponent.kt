package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * If you're familiar with AndroidViewModel - this is similar
 *
 * This component has [CoroutineScope] inside so you can use it as ViewModel's and etc
 */
abstract class AsyncComponent : Closeable, CoroutineScope, AbstractCoroutineContextElement(AsyncComponent), Runnable {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO
    val componentScope: CoroutineScope
        get() = this
    var queue: Queue<Runnable> = ConcurrentLinkedQueue()
    override fun run() {
        queue.poll()?.run()
    }

    override fun close() {
        coroutineContext.cancel()
    }

    companion object Key : CoroutineContext.Key<AsyncComponent>

    /**
     * Default implementation of [AsyncComponent]
     */
    class Default : AsyncComponent()
}
