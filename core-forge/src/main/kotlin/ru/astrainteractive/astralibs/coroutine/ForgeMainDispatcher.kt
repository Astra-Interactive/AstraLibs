package ru.astrainteractive.astralibs.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.loading.FMLEnvironment
import ru.astrainteractive.astralibs.async.CoroutineTimings
import ru.astrainteractive.klibs.mikro.core.util.tryCast
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class ForgeMainDispatcher : CoroutineDispatcher() {

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return true
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        DistExecutor.safeRunWhenOn(FMLEnvironment.dist) {
            val key = context.tryCast<AbstractCoroutineContextElement>()?.key
            val timingsKey = key?.tryCast<CoroutineTimings.Key>()
            val timedRunnable = timingsKey?.let(context::get)

            if (timedRunnable == null) {
                DistExecutor.SafeRunnable { block.run() }
            } else {
                timedRunnable.queue.add(block)
                DistExecutor.SafeRunnable { timedRunnable.run() }
            }
        }
    }
}
