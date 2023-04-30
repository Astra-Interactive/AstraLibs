package ru.astrainteractive.astralibs.orm

@Suppress("VariableNaming")
sealed interface DBSyntax {
    val NOT_NULL: String
    val AUTO_INCREMENT: String
    val PRIMARY_KEY: String

    object SQLite : DBSyntax {
        override val NOT_NULL: String = "NOT NULL"
        override val AUTO_INCREMENT: String = "AUTOINCREMENT"
        override val PRIMARY_KEY: String = "PRIMARY KEY"
    }

    object MySQL : DBSyntax {
        override val NOT_NULL: String = "NOT NULL"
        override val AUTO_INCREMENT: String = "AUTO_INCREMENT"
        override val PRIMARY_KEY: String = "PRIMARY KEY"
    }
}
