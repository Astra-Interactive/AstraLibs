package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.MainCoroutineDispatcher
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment
import ru.astrainteractive.astralibs.server.util.NeoForgeUtil
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

class NeoForgeMainDispatcher : MainCoroutineDispatcher() {
    override val immediate: MainCoroutineDispatcher
        get() = this
    private val executor = when (FMLEnvironment.dist) {
        Dist.CLIENT -> Minecraft.getInstance()
        Dist.DEDICATED_SERVER -> Executor { block ->
            NeoForgeUtil.requireServer().executeBlocking(block)
        }
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return true
    }

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
