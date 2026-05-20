package ru.astrainteractive.astralibs.coroutines

import ru.astrainteractive.klibs.mikro.core.coroutines.CoroutineFeature
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Coroutine context element that enables Spigot/Paper timing diagnostics to display a meaningful
 * name for a coroutine dispatched onto the Minecraft main thread.
 */
@Suppress("MaxLineLength")
class CoroutineTimings : AbstractCoroutineContextElement(Key), Runnable {
    data object Key : CoroutineContext.Key<CoroutineTimings>

    /** Queue of runnables assigned to this coroutine; polled one-at-a-time on each [run] call. */
    var queue: Queue<Runnable> = ConcurrentLinkedQueue()

    override fun run() {
        queue.poll()?.run()
    }
}

/** Adds a [CoroutineTimings] element to this scope so Spigot/Paper timings show the coroutine name. */
fun CoroutineFeature.withTimings(): CoroutineFeature.Default {
    return CoroutineFeature.Default(this.coroutineContext + CoroutineTimings())
}
