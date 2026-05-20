package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.MainCoroutineDispatcher
import ru.astrainteractive.astralibs.server.util.MinecraftUtil
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

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
