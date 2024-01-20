package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.Serializable

@Serializable
enum class KyoriComponentSerializerType {
    Json, Gson, Plain, MiniMessage, Legacy
}
