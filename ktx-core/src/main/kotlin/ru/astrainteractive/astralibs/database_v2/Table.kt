package ru.astrainteractive.astralibs.database_v2

import org.jetbrains.kotlin.gradle.utils.toSetOrEmpty
import ru.astrainteractive.astralibs.database.isConnected
import ru.astrainteractive.astralibs.database_v2.expression.Expression
import ru.astrainteractive.astralibs.database_v2.expression.SQLExpressionBuilder
import ru.astrainteractive.astralibs.utils.mapNotNull
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement


abstract class Table<T>(val tableName: String) {
    val _columns = mutableListOf<Column<*>>()

    abstract val id: Column<T>
    fun integer(name: String): Column<Int> {
        return Column<Int>(name, "INTEGER").also {
            _columns.add(it)
        }
    }

    fun long(name: String): Column<Long> {
        return Column<Long>(name, "INTEGER").also {
            _columns.add(it)
        }
    }

    fun text(name: String): Column<String> {
        return Column<String>(name, "TEXT").also {
            _columns.add(it)
        }
    }

    fun bool(name: String): Column<Int> {
        return Column<Int>(name, "BOOLEAN").also {
            _columns.add(it)
        }
    }

    fun float(name: String): Column<Float> {
        return Column<Float>(name, "REAL").also {
            _columns.add(it)
        }
    }

    fun double(name: String): Column<Double> {
        return Column<Double>(name, "REAL").also {
            _columns.add(it)
        }
    }

    fun byteArray(name: String): Column<ByteArray> {
        return Column<ByteArray>(name, "VARBINARY").also {
            _columns.add(it)
        }
    }


    private fun assertConnected(database: Database?): Connection {
        val database by DatabaseHolder
        val connection = database?.connection ?: throw DatabaseException.DatabaseNotConnectedException
        if (!connection.isConnected) throw DatabaseException.DatabaseNotConnectedException
        return connection
    }

    suspend fun create(database: Database? = DatabaseHolder.value) {
        val query = QueryGenerator.createCreateStatement(this)
        val connection = assertConnected(database)
        connection.prepareStatement(query).execute()
    }

    fun insert(database: Database? = DatabaseHolder.value, stmt: InsertStatement.() -> Unit): Int {
        val connection = assertConnected(database)
        val statement = InsertStatement(this).apply(stmt)
        val query = QueryGenerator.createInsertQuery(this, statement)
        val st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        statement.values.values.forEachIndexed { i, v ->
            st.setObject(i + 1, v)
        }
        st.executeUpdate()
        return st.generatedKeys.getInt(1)
    }

    fun <K : Entity<T>> wrap(it: ResultSet, constructor: () -> K): K {
        val entity = constructor()
        val table = entity.table
        table._columns.forEach { column ->
            val obj = it.getObject(column.name)
            entity[column as Column<Any?>] = obj
        }
        return entity
    }

    fun <K : Entity<T>> update(database: Database? = DatabaseHolder.value, entity: K) {
        val connection = assertConnected(database)

        val columns = entity.table._columns.filter { !it.primaryKey }
        val query = QueryGenerator.createUpdateStatement(entity)

        val stmnt = connection.prepareStatement(query)
        columns.forEachIndexed { index, column ->
            stmnt.setObject(index + 1, entity[column])
        }
        stmnt.executeUpdate()
    }

    fun <K : Entity<T>> delete(
        database: Database? = DatabaseHolder.value,
        op: SQLExpressionBuilder.() -> Expression<Boolean>
    ) {
        val op: Expression<Boolean> = SQLExpressionBuilder.op()
        val connection = assertConnected(database)
        val whenQuery = "WHERE ${SQLExpressionBuilder.resolveExpression(op)}"
        val query = "DELETE FROM $tableName $whenQuery"
        connection.prepareStatement(query).execute()
    }

    fun <K : Entity<T>> all(
        database: Database? = DatabaseHolder.value,
        constructor: () -> K,
    ): List<K> {
        val connection = assertConnected(database)
        val query = "SELECT * FROM ${tableName}"
        return connection.prepareStatement(query).executeQuery().mapNotNull {
            wrap(it, constructor)
        }
    }

    fun <K : Entity<T>> find(
        database: Database? = DatabaseHolder.value,
        constructor: () -> K,
        op: SQLExpressionBuilder.() -> Expression<Boolean>
    ): List<K> {
        val op: Expression<Boolean> = SQLExpressionBuilder.op()

        val connection = assertConnected(database)
        var whenQuery = ""
        whenQuery += SQLExpressionBuilder.resolveExpression(op)
        var query = "SELECT * FROM ${tableName}"
        if (whenQuery.isNotEmpty())
            query = "$query WHERE $whenQuery"
        return connection.prepareStatement(query).executeQuery().mapNotNull {
            wrap(it, constructor)
        }
    }

    fun count(
        database: Database? = DatabaseHolder.value,
        op: SQLExpressionBuilder.() -> Expression<Boolean>
    ): Int {
        val op: Expression<Boolean> = SQLExpressionBuilder.op()
        val connection = assertConnected(database)
        var whenQuery = ""
        whenQuery += SQLExpressionBuilder.resolveExpression(op)
        var query = "SELECT COUNT(*) AS total FROM ${tableName}"
        if (whenQuery.isNotEmpty())
            query = "$query WHERE $whenQuery"
        return connection.prepareStatement(query).executeQuery().getInt("total")
    }
}

internal fun <T> T.fixIfString(): T = if (this is String) "\"$this\"" as T else this