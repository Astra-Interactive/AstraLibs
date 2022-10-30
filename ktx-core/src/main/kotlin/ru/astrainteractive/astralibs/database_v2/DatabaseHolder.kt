package ru.astrainteractive.astralibs.database_v2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KProperty

object DatabaseHolder {
    private val writerDispatcher = Dispatchers.IO.limitedParallelism(1)
    private val databaseList: MutableSet<Database?> = mutableSetOf()
    private val connectedDBList: List<Database>
        get() = databaseList.filterNotNull().filter { it.isConnected }

    val value: Database?
        get() {
            if (connectedDBList.size > 1) throw DatabaseException.MultipleDatabaseConnections
            return connectedDBList.firstOrNull()
        }

    operator fun getValue(nothing: Nothing?, property: KProperty<*>): Database? = value

    suspend fun remember(database: Database) = withContext(writerDispatcher) {
        databaseList.add(database)
    }

    suspend fun forget(database: Database) = withContext(writerDispatcher) {
        databaseList.remove(database)
    }
}