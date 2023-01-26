package ru.astrainteractive.astralibs.orm

import java.sql.Connection
import java.sql.DriverManager


class DefaultDatabase(
    override val dbConnection: DBConnection,
    override val dbSyntax: DBSyntax
): Database {
    override var connection: Connection? = null
        private set
    override val isConnected: Boolean
        get() = connection?.isClosed == false

    override suspend fun openConnection() {
        Class.forName(dbConnection.driver)
        connection = when(dbConnection){
            is DBConnection.MySQL -> DriverManager.getConnection(dbConnection.url, dbConnection.username, dbConnection.password)
            is DBConnection.SQLite -> DriverManager.getConnection(dbConnection.url)
        }
    }

    override suspend fun closeConnection() {
        connection?.close()
        connection = null
    }
}