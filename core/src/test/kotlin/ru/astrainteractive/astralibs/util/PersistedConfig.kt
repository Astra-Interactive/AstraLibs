package ru.astrainteractive.astralibs.util

import kotlinx.serialization.Serializable

/** Serializable fixture for file-persistence tests. */
@Serializable
data class PersistedConfig(
    val name: String = "default",
    val count: Int = 0
)
