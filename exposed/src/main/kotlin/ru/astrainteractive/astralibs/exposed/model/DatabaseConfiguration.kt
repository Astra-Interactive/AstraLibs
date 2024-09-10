package ru.astrainteractive.astralibs.exposed.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed interface DatabaseConfiguration {
    val driver: String
    val arguments: List<String>

    @Transient
    val stringArgument: String
        get() = if (arguments.isEmpty()) arguments.joinToString(separator = "&", prefix = "?") else ""

    @SerialName("MySql")
    @Serializable
    @Suppress("LongParameterList")
    class MySql(
        val host: String,
        val port: Int,
        val user: String,
        val password: String,
        val name: String,
        override val driver: String = "com.mysql.cj.jdbc.Driver",
        override val arguments: List<String>
    ) : DatabaseConfiguration

    @SerialName("H2")
    @Serializable
    data class H2(
        val name: String,
        override val driver: String = "org.h2.Driver",
        override val arguments: List<String>
    ) : DatabaseConfiguration

    @SerialName("SQLite")
    @Serializable
    data class SQLite(
        val name: String,
        override val driver: String = "org.sqlite.JDBC",
        override val arguments: List<String>
    ) : DatabaseConfiguration
}
