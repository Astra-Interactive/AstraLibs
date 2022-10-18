package ru.astrainteractive.astralibs.database_v2

sealed class DatabaseException(message: String? = null) : Exception(message) {
    object DatabaseNotConnectedException : DatabaseException("Database not connected")
}