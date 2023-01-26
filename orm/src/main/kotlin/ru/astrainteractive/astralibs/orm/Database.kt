package ru.astrainteractive.astralibs.orm

import java.sql.Connection

interface Database{
    val dbConnection: DBConnection
    val dbSyntax: DBSyntax
    val isConnected: Boolean
    val connection: Connection?
    suspend fun openConnection()
    suspend fun closeConnection()
}