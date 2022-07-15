package com.astrainteractive.astralibs.async

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.utils.catching
import org.bukkit.Bukkit

/**
 * If you don't want to extend your class, just use [AsyncHelper.runBackground]
 */
object AsyncHelper : AsyncTask {
    /**
     * Call sync method from background task
     */
    inline fun <T> callSyncMethod(crossinline block: () -> T) = catching {
        Bukkit.getScheduler().callSyncMethod(AstraLibs.instance) {
            block.invoke()
        }
    }
}