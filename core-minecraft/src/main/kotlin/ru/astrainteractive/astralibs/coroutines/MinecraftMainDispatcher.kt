package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.MainCoroutineDispatcher
import ru.astrainteractive.astralibs.server.util.MinecraftUtil
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

/**
 * [MainCoroutineDispatcher] that schedules continuations on the Minecraft server main thread.
 *
 * If [CoroutineTimings] is present in the context, the runnable is routed through it so
 * server profilers display a meaningful task name. Every dispatch goes through the server
 * task queue — even when already on the main thread — to preserve ordering.
 */
class MinecraftMainDispatcher : MainCoroutineDispatcher() {
    override val immediate: MainCoroutineDispatcher
        get() = this

    private val executor = Executor { block ->
        MinecraftUtil.requireServer().executeBlocking(block)
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean = true

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        val coroutineTimings = context[CoroutineTimings.Key]
        if (coroutineTimings == null) {
            executor.execute(block)
        } else {
            coroutineTimings.queue.add(block)
            executor.execute(coroutineTimings)
        }
    }
}
