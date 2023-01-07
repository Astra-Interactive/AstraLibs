package ru.astrainteractive.astralibs.orm.database

import ru.astrainteractive.astralibs.orm.Database
import ru.astrainteractive.astralibs.orm.exception.DatabaseException
import ru.astrainteractive.astralibs.orm.expression.Expression
import ru.astrainteractive.astralibs.orm.expression.SQLExpressionBuilder
import ru.astrainteractive.astralibs.orm.query.*
import ru.astrainteractive.astralibs.orm.statement.DBStatement
import ru.astrainteractive.astralibs.utils.mapNotNull
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement


abstract class Table<T>(val tableName: String) {
    private val _columns = mutableListOf<Column<*>>()
    val columns: List<Column<*>>
        get() = _columns

    abstract val id: Column<T>
    fun integer(name: String): Column<Int> = Column.IntColumn(name).also(_columns::add)

    fun long(name: String): Column<Long> = Column.LongColumn(name).also(_columns::add)

    fun text(name: String): Column<String> = Column.StringColumn(name).also(_columns::add)

    fun bool(name: String): Column<Int> = Column.BoolColumn(name).also(_columns::add)

    fun float(name: String): Column<Float> = Column.FloatColumn(name).also(_columns::add)

    fun double(name: String): Column<Double> = Column.DoubleColumn(name).also(_columns::add)

    fun byteArray(name: String): Column<ByteArray> = Column.ByteArrayColumn(name).also(_columns::add)


    private fun assertConnected(database: Database): Connection {
        val connection = database?.connection ?: throw DatabaseException.DatabaseNotConnectedException
        if (database?.isConnected != true) throw DatabaseException.DatabaseNotConnectedException
        return connection
    }

    suspend fun create(database: Database) {
        val query = CreateQuery(this).generate()
        val connection = assertConnected(database)
        connection.prepareStatement(query).also {
            it.execute()
            it.close()
        }
    }

    fun insert(
        database: Database,
        stmt: DBStatement.() -> Unit
    ): Int {
        val connection = assertConnected(database)
        val dbstatement = DBStatement().apply(stmt)
        val query = InsertQuery(this, dbstatement).generate()
        val statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        dbstatement.values.values.forEachIndexed { i, v ->
            statement.setObject(i + 1, v)
        }
        statement.executeUpdate()
        val key = statement.generatedKeys.getInt(1)
        statement.close()
        return key
    }

    fun <K : Entity<T>> wrap(it: ResultSet, constructor: Constructable<K>): K {
        val entity = constructor.construct()
        val table = entity.table
        table._columns.forEach { column ->
            val obj = it.getObject(column.name)
            entity[column as Column<Any?>] = obj
        }
        return entity
    }

    fun <K : Entity<T>> update(database: Database, entity: K,) {
        val connection = assertConnected(database)

        val columns = entity.table._columns.filter { !it.primaryKey }
        val query = UpdateQuery(this,entity).generate()

        val statement = connection.prepareStatement(query).also { statement ->
            columns.forEachIndexed { index, column ->
                statement.setObject(index + 1, entity[column])
            }
        }
        statement.also {
            it.executeUpdate()
            it.close()
        }
    }

    fun <K : Entity<T>> delete(
        database: Database,
        op: SQLExpressionBuilder.() -> Expression<Boolean>
    ) {
        val connection = assertConnected(database)
        val query = DeleteQuery(this, op).generate()
        val statement = connection.prepareStatement(query)
        statement.also {
            it.execute()
            it.close()
        }
    }

    fun <K : Entity<T>> all(
        database: Database,
        constructor: Constructable<K>,
    ): List<K> {
        val connection = assertConnected(database)
        val query = SelectQuery(this).generate()
        val statement =connection.prepareStatement(query)
        val wrapperd = statement.executeQuery().mapNotNull {
            wrap(it, constructor)
        }
        statement.close()
        return wrapperd
    }

    fun <K : Entity<T>> find(
        database: Database,
        constructor: Constructable<K>,
        op: SQLExpressionBuilder.() -> Expression<Boolean>
    ): List<K> {
        val query = SelectQuery(this, op).generate()
        val connection = assertConnected(database)
        val statement = connection.prepareStatement(query)
        val wrapped = statement.executeQuery().mapNotNull {
            wrap(it, constructor)
        }
        statement.close()
        return wrapped
    }

    fun count(
        database: Database,
        op: SQLExpressionBuilder.() -> Expression<Boolean>
    ): Int {
        val connection = assertConnected(database)
        val query = CountQuery(this, op).generate()
        val statement = connection.prepareStatement(query)
        val total =  statement.executeQuery().getInt("total")
        statement.close()
        return total
    }
}

