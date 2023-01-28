package ru.astrainteractive.astralibs.orm.exception

sealed class DatabaseException(message: String? = null) : Exception(message) {
    object DatabaseNotConnectedException : DatabaseException("Database not connected")
    object MultipleDatabaseConnections : DatabaseException("You are connected to multiple databases, but didn't specify exact")
    object NoIdReturned : DatabaseException("Could not fetch row id")
    object UniqueTextError : DatabaseException("TEXT Can not be unique")
}