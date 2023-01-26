package ru.astrainteractive.astralibs.orm

sealed class DBConnection(val driver: String) {
    abstract val url: String
    class SQLite(val dbName: String) : DBConnection("org.sqlite.JDBC", ){
        override val url: String
            get() = "jdbc:sqlite:$dbName"
    }
    class MySQL(
        val database: String,
        private val ip: String,
        private val port: Int,
        val username: String,
        val password: String
    ): DBConnection("com.mysql.cj.jdbc.Driver"){
        override val url: String
            get() = "jdbc:mysql://$ip:$port"

    }
}

