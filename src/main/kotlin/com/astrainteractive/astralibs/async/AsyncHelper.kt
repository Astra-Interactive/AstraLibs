package com.astrainteractive.astralibs.async

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.catching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Bukkit

/**
 * If you don't want to extend your class, just use [AsyncHelper.runBackground]
 */
object AsyncHelper : AsyncTask {
    /**
     * Run task in background thread with selected [CoroutineDispatcher]
     */
    @Deprecated(
        "Use AsyncHelper.launch{} instead",
        ReplaceWith("launch(scope) { block.invoke(this) }", "kotlinx.coroutines.launch")
    )
    inline fun <T> runBackground(
        scope: CoroutineDispatcher = Dispatchers.IO,
        crossinline block: suspend CoroutineScope.() -> T
    ) {
        launch(scope) {
            block.invoke(this)
        }
    }

    /**
     * Call sync method from background task
     */
    inline fun <T> callSyncMethod(crossinline block: () -> T) = catching {
        Bukkit.getScheduler().callSyncMethod(AstraLibs.instance) {
            block.invoke()
        }
    }
}