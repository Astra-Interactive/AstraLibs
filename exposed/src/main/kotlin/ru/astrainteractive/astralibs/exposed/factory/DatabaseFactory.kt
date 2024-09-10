package ru.astrainteractive.astralibs.exposed.factory

import org.jetbrains.exposed.sql.Database
import ru.astrainteractive.astralibs.exposed.model.DatabaseConfiguration
import java.io.File

class DatabaseFactory(private val dataFolder: File) {
    fun create(dbConfig: DatabaseConfiguration): Database {
        return when (dbConfig) {
            is DatabaseConfiguration.H2 -> Database.connect(
                url = "jdbc:sqlite:${dataFolder.resolve("${dbConfig.name}.db").absolutePath}${dbConfig.stringArgument}",
                driver = dbConfig.driver
            )

            is DatabaseConfiguration.MySql -> Database.connect(
                url = "jdbc:mysql://${dbConfig.host}:${dbConfig.port}/${dbConfig.name}${dbConfig.stringArgument}",
                driver = dbConfig.driver,
                user = dbConfig.user,
                password = dbConfig.password
            )

            is DatabaseConfiguration.SQLite -> Database.connect(
                url = "jdbc:sqlite:${dataFolder.resolve("${dbConfig.name}.db").absolutePath}${dbConfig.stringArgument}",
                driver = dbConfig.driver
            )
        }
    }
}
