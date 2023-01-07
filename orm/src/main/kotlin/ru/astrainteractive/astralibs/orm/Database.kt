package ru.astrainteractive.astralibs.orm

import java.sql.Connection
import java.sql.DriverManager


class Database {
    var connection: Connection? = null
        private set
    val isConnected: Boolean
        get() = connection?.isClosed == false

    suspend fun openConnection(path: String, dbConnection: DBConnection): Connection? {
        Class.forName(dbConnection.driver)
        connection = DriverManager.getConnection(dbConnection.url + path)
        return connection
    }

    suspend fun closeConnection() {
        connection?.close()
        connection = null
    }
}