package ru.astrainteractive.astralibs.util

inline fun <reified T> Any.cast() = this as T
inline fun <reified T> Any.tryCast() = this as? T
