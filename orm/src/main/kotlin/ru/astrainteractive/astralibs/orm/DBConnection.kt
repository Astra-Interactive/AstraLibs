package ru.astrainteractive.astralibs.orm

sealed class DBConnection(val driver: String) {
    abstract val url: String

    class SQLite(val dbName: String) : DBConnection("org.sqlite.JDBC") {
        override val url: String
            get() = "jdbc:sqlite:$dbName"
    }

    class InMemory(val dbName: String) : DBConnection("org.sqlite.JDBC") {
        override val url: String = "jdbc:sqlite:file:$dbName?mode=memory&cache=shared"
    }

    /**
     * @property database database name
     * @property ip ip address of your MySql server
     * @property port port of your MySql server
     * @property username username
     * @property password password
     * @property sessionVariables - session variables of MySql connection. Example: sql_mode=''
     */
    class MySQL(
        val database: String,
        private val ip: String,
        private val port: Int,
        val username: String,
        val password: String,
        vararg val sessionVariables: String
    ) : DBConnection("com.mysql.cj.jdbc.Driver") {
        override val url: String
            get() {
                val sv = if (sessionVariables.isEmpty()) {
                    ""
                } else {
                    sessionVariables.joinToString("&", "?sessionVariables=") { it }
                }

                return "jdbc:mysql://$ip:$port/$database$sv"
            }
    }
}
