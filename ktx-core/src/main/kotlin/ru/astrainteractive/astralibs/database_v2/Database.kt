package ru.astrainteractive.astralibs.database_v2

import kotlinx.coroutines.withContext
import ru.astrainteractive.astralibs.database.isConnected
import java.sql.Connection
import java.sql.DriverManager


class Database {
    var connection: Connection? = null
        private set
    val isConnected: Boolean
        get() = connection?.isConnected == true

    suspend fun openConnection(url: String, driver: String): Connection? {
        Class.forName(driver)
        connection = DriverManager.getConnection(url)
        DatabaseHolder.remember(this@Database)
        return connection
    }

    suspend fun closeConnection() {
        connection?.close()
        connection = null
        DatabaseHolder.forget(this@Database)
    }
}