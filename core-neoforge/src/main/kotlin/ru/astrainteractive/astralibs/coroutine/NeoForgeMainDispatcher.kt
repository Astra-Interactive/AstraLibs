package ru.astrainteractive.astralibs.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer
import net.minecraft.util.thread.ReentrantBlockableEventLoop
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.server.ServerLifecycleHooks
import ru.astrainteractive.astralibs.async.CoroutineTimings
import ru.astrainteractive.klibs.mikro.core.util.tryCast
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class NeoForgeMainDispatcher : CoroutineDispatcher() {

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return true
    }

    private fun getExecutor(): ReentrantBlockableEventLoop<out Runnable> {
        return when (FMLEnvironment.dist) {
            Dist.CLIENT -> Minecraft.getInstance()
            Dist.DEDICATED_SERVER -> {
                var server: MinecraftServer
                runBlocking {
                    while (true) {
                        ServerLifecycleHooks.getCurrentServer()
                            ?.let { currentServer ->
                                server = currentServer
                                break
                            }
                        yield()
                    }
                }
                server
            }
        }
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        getExecutor().execute {
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
