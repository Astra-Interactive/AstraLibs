package com.astrainteractive.astralibs

import com.astrainteractive.astralibs.async.AsyncHelper
import java.io.Closeable
import kotlin.concurrent.timer


/**
 * Calculating cooldown for anything where [K] is key
 */
class Cooldown<K>(val maxTime: Long = 60):AutoCloseable {
    private val map = mutableMapOf<K, Long>()
    val constMap: Map<K, Long>
        get() = synchronized(this) { map.toMap() }
    val scheduler = kotlin.concurrent.timer(null,true,0L,maxTime){
        clearOldCooldowns()
    }


    /**
     * Set starded cooldown time for [K] for this [System.currentTimeMillis]
     */
    fun setCooldown(key: K) = synchronized(this) {
        map[key] = System.currentTimeMillis()
    }

    /**
     * Remove key from cooldown map
     */
    fun removeCooldown(key: K) = synchronized(this) {
        map.remove(key)
    }

    /**
     * Clear all keys, which passed time more than [maxTime]
     */
    fun clearOldCooldowns() = AsyncHelper.runBackground {
        constMap.forEach { (k, v) ->
            if (getPassedTime(v)> maxTime)
                removeCooldown(k)
        }
    }
    companion object{
        /**
         * @return time passed since [time] in milliseconds
         */
        fun getPassedTime(time:Long) = System.currentTimeMillis() - time
    }

    /**
     * Check cooldown on object [K]
     * @return boolean true if cooldown still exist false if there is no active cooldown
     */
    fun hasCooldown(key: K, time: Int) = hasCooldown(key, time.toLong())
    fun hasCooldown(key: K, time: Long?): Boolean {
        time ?: return false
        val started = map[key] ?: return false
        if (getPassedTime(started) > time) {
            removeCooldown(key)
            return true
        }
        return false
    }

    override fun close() {
        scheduler.cancel()
    }
}