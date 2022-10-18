package ru.astrainteractive.astralibs.database_v2

import ru.astrainteractive.astralibs.database.isConnected
import ru.astrainteractive.astralibs.utils.mapNotNull
import java.sql.Connection

abstract class Table<T>(val tableName: String) {
    val _columns = mutableListOf<Column<*>>()

    abstract val id: Column<T>
    fun integer(name: String): Column<Int> {
        return Column<Int>(name, "INTEGER").also {
            _columns.add(it)
        }
    }

    fun text(name: String): Column<String> {
        return Column<String>(name, "TEXT").also {
            _columns.add(it)
        }
    }


    private fun assertConnected(database: Database): Connection {
        val connection = database.connection ?: throw DatabaseException.DatabaseNotConnectedException
        if (!connection.isConnected) throw DatabaseException.DatabaseNotConnectedException
        return connection
    }

    suspend fun create(database: Database) {
        val query = QueryGenerator.createCreateStatement(this)
        val connection = assertConnected(database)
        connection.prepareStatement(query).execute()
    }

    fun insert(database: Database, stmt: InsertStatement.() -> Unit) {
        val connection = assertConnected(database)
        val statement = InsertStatement(this).apply(stmt)
        val query = QueryGenerator.createInsertQuery(this, statement)
        val st = connection.prepareStatement(query)
        statement.values.values.forEachIndexed { i, v ->
            st.setObject(i + 1, v)
        }
        st.executeUpdate()
    }


    fun <K : Entity<T>> select(database: Database, query: String, constructor: () -> K): List<K> {
        val connection = assertConnected(database)
        val resultRow = connection.prepareStatement(query).executeQuery()
        return resultRow.mapNotNull {
            val entity = constructor()
            val table = entity.table
            table._columns.forEach { column ->
                val obj = it.getObject(column.name)
                entity[column as Column<Any?>] = obj
            }
            entity
        }
    }
}


