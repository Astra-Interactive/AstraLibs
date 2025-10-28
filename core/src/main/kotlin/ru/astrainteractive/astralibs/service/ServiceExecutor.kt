package ru.astrainteractive.astralibs.service

fun interface ServiceExecutor {
    suspend fun doWork()
}
