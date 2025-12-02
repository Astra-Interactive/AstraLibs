package ru.astrainteractive.astralibs.coroutine

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
            NeoForgeUtil.requireServer().executeBlocking(block) // .doRunTask(task)
        }
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return true
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        executor.execute(block)
    }
}
