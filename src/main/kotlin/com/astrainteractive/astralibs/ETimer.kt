package com.astrainteractive.astralibs

object ETimer {
    private val timers = mutableMapOf<String,Long>()
    fun startTimer(key:String){
        timers[key] = System.currentTimeMillis()
    }
    fun endTimer(key:String){
        println("Task $key enden in ${(System.currentTimeMillis().minus(timers[key]?:return))/1000.0}s")
        timers.remove(key)
    }
    fun timer(key: String){
        if (timers.containsKey(key))
            endTimer(key)
        else
            startTimer(key)
    }
}