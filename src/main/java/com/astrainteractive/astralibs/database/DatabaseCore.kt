package com.astrainteractive.astralibs.database

import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.utils.catching
import com.astrainteractive.astralibs.utils.mapNotNull
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

val Connection?.isConnected: Boolean
    get() = this?.isClosed != true

val String.sqlString: String
    get() = "\"$this\""

abstract class DatabaseCore {
    abstract val connectionBuilder: () -> Connection?
    val connection by lazy { catching { connectionBuilder() } }


    abstract suspend fun onEnable()
    suspend fun close() = catching {
        connection?.close()
        connection?.close()
    }

    inline fun <reified T> update(instance: T, debugQuery: Boolean = false) =
        update(T::class.java, instance, debugQuery = debugQuery)

    inline fun <reified T> delete(instance: T, debugQuery: Boolean = false) =
        delete(T::class.java, instance, debugQuery = debugQuery)

    inline fun <reified T> select(where: String = "", debugQuery: Boolean = false) =
        select(T::class.java, where, debugQuery = debugQuery)

    inline fun <reified T> insert(instance: T, debugQuery: Boolean = false) =
        insert(T::class.java, instance, debugQuery = debugQuery)

    inline fun <reified T> createTable(debugQuery: Boolean = false) =
        createTable(T::class.java, debugQuery = debugQuery)

    inline fun <reified T> querySelect(query: String, noinline rsBuilder: (ResultSet) -> T?) =
        querySelect(T::class.java, query, rsBuilder)

    fun <T> update(clazz: Class<out T>, instance: T, debugQuery: Boolean = false): Boolean? {
        val info = AnnotationUtils.EntityInfo.create(clazz, instance)
        val primaryKeyInfo =
            info?.columns?.firstOrNull { it.primaryKey != null } ?: throw Exception("Primary key not found")

        val entries = info?.columns?.mapNotNull {
            if (it.primaryKey != null) null
            else "${it.columnInfo.name}=${it.sqlFieldValue}"
        }?.joinToString(", ")

        val query =
            "UPDATE ${info?.entity?.tableName} SET $entries WHERE ${primaryKeyInfo.columnInfo.name}=${primaryKeyInfo.sqlFieldValue}"
        if (debugQuery) Logger.log(query, TAG)
        return connection?.prepareStatement(query)?.execute()
    }

    fun <T> delete(clazz: Class<out T>, instance: T, debugQuery: Boolean = false): Boolean? {
        val info = AnnotationUtils.EntityInfo.create(clazz, instance)
        val primaryKeyInfo =
            info?.columns?.firstOrNull { it.primaryKey != null } ?: throw Exception("Primary key not found")
        val query =
            "DELETE FROM ${info?.entity?.tableName} WHERE ${primaryKeyInfo.columnInfo.name}=${primaryKeyInfo.sqlFieldValue}"
        if (debugQuery) Logger.log(query, TAG)
        return connection?.prepareStatement(query)?.execute()
    }

    fun <T> select(clazz: Class<out T>, where: String = "", debugQuery: Boolean = false): List<T>? {
        val info = AnnotationUtils.EntityInfo.create(clazz)
        val query = "SELECT * FROM ${info?.entity?.tableName} $where"
        if (debugQuery) Logger.log(query, TAG)
        return connection?.createStatement()?.executeQuery(query)?.mapNotNull { rs ->
            fromResultSet(clazz, info, rs)
        }
    }


    fun <T> querySelect(clazz: Class<out T>, query: String = "", rsBuilder: (ResultSet) -> T?): List<T>? {
        return connection?.createStatement()?.executeQuery(query)?.mapNotNull { rs ->
            rsBuilder.invoke(rs)
        }
    }


    fun <T> insert(clazz: Class<out T>, instance: T, debugQuery: Boolean = false): Long? {
        val info = AnnotationUtils.EntityInfo.create(clazz, instance)
        val keys = info?.columns?.mapNotNull {
            if (it.primaryKey != null) null
            else it.columnInfo.name
        }?.joinToString(", ", "(", ")")

        val values = info?.columns?.filter { it.primaryKey==null }?.map {
            it.sqlFieldValue
        }?.joinToString(", ", "(", ")")

        val query = "INSERT INTO ${info?.entity?.tableName} $keys VALUES $values"
        if (debugQuery) Logger.log(query, TAG)

        val prepared =
            connection?.prepareStatement(query, Statement.RETURN_GENERATED_KEYS).apply { this?.executeUpdate() }
        return prepared?.generatedKeys?.getLong(1)
    }


    fun <T> createTable(clazz: Class<out T>, debugQuery: Boolean = false): Boolean? {
        val info = AnnotationUtils.EntityInfo.create(clazz)
        val keys = info?.columns?.joinToString(",", "(", ")") {
            buildList {
                add("${it.columnInfo.name} ${AnnotationUtils.resolveType(it.parameter)}")
                it.primaryKey?.let {
                    add("PRIMARY KEY")
                    if (it.autoIncrement)
                        add("AUTOINCREMENT")
                }
                if (!it.columnInfo.nullable) add("NOT NULL")
                if (it.columnInfo.unique) add("UNIQUE")
            }.joinToString(" ")
        }
        val query = "CREATE TABLE IF NOT EXISTS ${info?.entity?.tableName} $keys"
        if (debugQuery) Logger.log(query, TAG)
        return connection?.prepareStatement(query)?.execute()
    }

    val TAG = "DatabaseCore"
}

