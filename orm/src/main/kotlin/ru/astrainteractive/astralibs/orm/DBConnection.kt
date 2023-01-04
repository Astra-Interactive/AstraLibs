package ru.astrainteractive.astralibs.orm

sealed class DBConnection(val driver: String, val url: String) {
    object SQLite : DBConnection("org.sqlite.JDBC", "jdbc:sqlite:")
}
