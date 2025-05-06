package ru.astrainteractive.astralibs.util

import kotlinx.coroutines.yield

suspend fun awaitForCompletion(isCompleted: suspend () -> Boolean) {
    while (!isCompleted.invoke()) {
        yield()
    }
}
