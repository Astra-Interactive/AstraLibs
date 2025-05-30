package ru.astrainteractive.astralibs.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.util.LogicalSidedProvider
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.loading.FMLEnvironment
import kotlin.coroutines.CoroutineContext

object ForgeMainDispatcher : MainCoroutineDispatcher() {
    private val dispatcher: CoroutineDispatcher by lazy {
        when (FMLEnvironment.dist) {
            Dist.CLIENT -> LogicalSidedProvider.WORKQUEUE.get(LogicalSide.CLIENT)
            Dist.DEDICATED_SERVER -> LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER)
        }.asCoroutineDispatcher()
    }

    override val immediate: MainCoroutineDispatcher get() = this

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatcher.dispatch(context, block)
    }

//    override fun dispatch(context: CoroutineContext, block: Runnable) {
//        DistExecutor.safeRunWhenOn(FMLEnvironment.dist) {
//            DistExecutor.SafeRunnable { block.run() }
//        }
//    }
}
