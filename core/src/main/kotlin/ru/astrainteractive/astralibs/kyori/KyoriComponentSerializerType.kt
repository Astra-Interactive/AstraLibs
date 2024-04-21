package ru.astrainteractive.astralibs.kyori

import kotlinx.serialization.Serializable

@Serializable
enum class KyoriComponentSerializerType {
    Json, Gson, Plain, MiniMessage, Legacy
}
