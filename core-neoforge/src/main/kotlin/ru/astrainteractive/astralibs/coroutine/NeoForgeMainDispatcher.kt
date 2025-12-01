package ru.astrainteractive.astralibs.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment
import ru.astrainteractive.astralibs.async.CoroutineTimings
import ru.astrainteractive.astralibs.server.util.NeoForgeUtil
import ru.astrainteractive.klibs.mikro.core.util.tryCast
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class NeoForgeMainDispatcher : CoroutineDispatcher() {
    private val executor = when (FMLEnvironment.dist) {
        Dist.CLIENT -> Minecraft.getInstance()
        Dist.DEDICATED_SERVER -> NeoForgeUtil.requireServer()
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return true
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        executor.execute {
            val key = context.tryCast<AbstractCoroutineContextElement>()?.key
            val timingsKey = key?.tryCast<CoroutineTimings.Key>()
            val timedRunnable = timingsKey?.let(context::get)

            if (timedRunnable == null) {
                block.run()
            } else {
                timedRunnable.queue.add(block)
                timedRunnable.run()
            }
        }
    }
}
