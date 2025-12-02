package ru.astrainteractive.astralibs.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import net.minecraft.client.Minecraft
import net.minecraft.server.TickTask
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment
import ru.astrainteractive.astralibs.server.util.NeoForgeUtil
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

class NeoForgeMainDispatcher : CoroutineDispatcher() {
    private val executor = when (FMLEnvironment.dist) {
        Dist.CLIENT -> Minecraft.getInstance()
        Dist.DEDICATED_SERVER -> Executor { block ->
            val task = TickTask(Int.MAX_VALUE, block)
            NeoForgeUtil.requireServer().doRunTask(task)
        }
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return true
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        executor.execute(block)
    }
}
