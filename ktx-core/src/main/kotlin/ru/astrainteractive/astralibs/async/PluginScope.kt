package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.*
import ru.astrainteractive.astralibs.architecture.AsyncComponent
import ru.astrainteractive.astralibs.architecture.CloseableCoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * If you don't want to extend your class, just use [PluginScope.launch]
 * Don't forget to call [PluginScope.cancel] when disabling your plugin
 */
object PluginScope : AsyncTask, AsyncComponent() {
    override val coroutineContext: CoroutineContext = CloseableCoroutineScope(SupervisorJob() + Dispatchers.IO).coroutineContext
}

