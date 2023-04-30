import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.astrainteractive.astralibs.orm.DBConnection
import ru.astrainteractive.astralibs.orm.DBSyntax
import ru.astrainteractive.astralibs.orm.Database
import ru.astrainteractive.astralibs.orm.DefaultDatabase

object Resource {
    @Serializable
    private class Connection(
        val database: String,
        val ip: String,
        val port: Int,
        val username: String,
        val password: String
    )

    @Suppress("UnusedPrivateMember")
    private fun readMySqlConnection(): Database {
        val fileContent = this::class.java.classLoader.getResource("mysql.json").readText()
        val connection: Connection = Json.decodeFromString(fileContent)
        val dbConnection = DBConnection.MySQL(
            database = connection.database,
            ip = connection.ip,
            port = connection.port,
            username = connection.username,
            password = connection.password,
        )
        return DefaultDatabase(dbConnection, DBSyntax.MySQL)
    }

    private fun sqliteConnection(): Database {
        return DefaultDatabase(DBConnection.SQLite("db.db"), DBSyntax.SQLite)
    }

    fun getDatabase(): Database {
//        return readMySqlConnection()
        return sqliteConnection()
    }
}
