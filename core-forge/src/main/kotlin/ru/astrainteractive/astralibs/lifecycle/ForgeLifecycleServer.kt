package ru.astrainteractive.astralibs.lifecycle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.minecraftforge.event.server.ServerStartedEvent
import net.minecraftforge.event.server.ServerStoppingEvent
import net.minecraftforge.eventbus.api.EventPriority
import ru.astrainteractive.astralibs.event.flowEvent
import ru.astrainteractive.astralibs.server.util.ForgeUtil
import ru.astrainteractive.klibs.mikro.core.logging.Logger

abstract class ForgeLifecycleServer : Lifecycle, Logger {
    private val unconfinedScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    val serverStartedEvent = flowEvent<ServerStartedEvent>(EventPriority.HIGHEST)
        .onEach { onEnable() }
        .catch { throwable ->
            error(throwable) { "#serverStartedEvent ${throwable.localizedMessage}" }
        }
        .launchIn(unconfinedScope)

    val serverStoppingEvent = flowEvent<ServerStoppingEvent>(EventPriority.HIGHEST)
        .onEach { onDisable() }
        .onEach { unconfinedScope.cancel() }
        .catch { throwable ->
            error(throwable) { "#serverStartedEvent ${throwable.localizedMessage}" }
        }
        .launchIn(unconfinedScope)

    init {
        ForgeUtil.bootstrap()
    }
}
