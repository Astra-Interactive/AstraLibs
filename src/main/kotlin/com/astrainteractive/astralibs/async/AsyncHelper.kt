package com.astrainteractive.astralibs.async

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.catchingNoStackTrace
import kotlinx.coroutines.CoroutineDispatcher
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
    inline fun <T> runBackground(scope: CoroutineDispatcher = Dispatchers.IO, crossinline block: () -> T) {
        launch(scope) {
            block.invoke()
        }
    }

    /**
     * Call sync method from background task
     */
    inline fun <T> callSyncMethod(crossinline block: () -> T) = catchingNoStackTrace {
        Bukkit.getScheduler().callSyncMethod(AstraLibs.instance) {
            block.invoke()
        }
    }
}