package ru.astrainteractive.astralibs.coroutines.model

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Coroutine context element that enables Spigot/Paper timing diagnostics to display a meaningful
 * name for a coroutine dispatched onto the Minecraft main thread.
 */
@Suppress("MaxLineLength")
public class CoroutineTimings : AbstractCoroutineContextElement(Key), Runnable {
    public data object Key : CoroutineContext.Key<CoroutineTimings>

    /** Queue of runnables assigned to this coroutine; polled one-at-a-time on each [run] call. */
    public var queue: Queue<Runnable> = ConcurrentLinkedQueue()

    override fun run() {
        queue.poll()?.run()
    }
}
