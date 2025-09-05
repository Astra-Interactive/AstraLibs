package ru.astrainteractive.astralibs.async

import ru.astrainteractive.klibs.mikro.core.coroutines.CoroutineFeature
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * The spigot timings require a reference to the runnable to display the name of timings correctly.
 * Now, Kotlin Coroutines does not allow to directly pass a runnable object, because a single coroutine
 * may consist out of multiple runnables. This class is a workaround coroutine context element, which can be passed
 * along the [minecraftDispatcher] to display a valid name for the coroutine.
 * @see <a href="https://github.com/Shynixn/MCCoroutine/blob/c7de1e8b6409351a67a74d1c2c87887e98f9e989/mccoroutine-folia-api/src/main/java/com/github/shynixn/mccoroutine/folia/CoroutineTimings.kt#L23">Original</a>
 */
@Suppress("MaxLineLength")
class CoroutineTimings : AbstractCoroutineContextElement(Key()), Runnable {
    /**
     * Key identifier of the context element.
     */
    private class Key : CoroutineContext.Key<CoroutineTimings>

    /**
     *  Multiple tasks can be assigned to a single coroutine. We implement this by a queue.
     */
    var queue: Queue<Runnable> = ConcurrentLinkedQueue()

    /**
     * When an object implementing interface `Runnable` is used
     * to create a thread, starting the thread causes the object's
     * `run` method to be called in that separately executing
     * thread.
     *
     *
     * The general contract of the method `run` is that it may
     * take any action whatsoever.
     *
     * @see java.lang.Thread.run
     */
    override fun run() {
        queue.poll()?.run()
    }
}

fun CoroutineFeature.withTimings(): CoroutineFeature.Default {
    return CoroutineFeature.Default(this.coroutineContext + CoroutineTimings())
}
