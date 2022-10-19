package ru.astrainteractive.astralibs.database_v2

object QueryGenerator {

    fun createInsertQuery(table: Table<*>, statement: InsertStatement): String {
        val keys = statement.values.keys.joinToString(", ", "(", ")") { it.name }
        val valuesQuery = statement.values.values.joinToString(", ", "(", ")") { "?" }
        val query = "INSERT INTO ${table.tableName} $keys VALUES $valuesQuery"
        return query
    }

    fun <T, K : Entity<T>> createUpdateStatement(entity: K): String {
        val tableName = entity.table.tableName
        val primaryKey = entity.table.id
        val primaryKeyValue = entity[primaryKey]
        val columns = entity.table._columns.filter { !it.primaryKey }
        val keys = columns.joinToString(",") {
            "${it.name}=?"
        }
        val query =
            "UPDATE $tableName SET $keys WHERE ${primaryKey.name}=${primaryKeyValue}"
        return query
    }
    fun <T, K : Entity<T>> createDeleteStatement(entity: K): String {
        val tableName = entity.table.tableName
        val primaryKey = entity.table.id
        val primaryKeyValue = entity[primaryKey]
        val query =
            "DELETE FROM $tableName WHERE ${primaryKey.name}=${primaryKeyValue}"
        return query
    }

    fun createCreateStatement(table: Table<*>): String {
        val keys = table._columns.joinToString(",", "(", ")") { column ->
            buildList<String> {
                add("${column.name} ${column.type}")
                if (column.primaryKey) {
                    add("PRIMARY KEY")
                    if (column.autoIncrement)
                        add("AUTOINCREMENT")
                }
                if (!column.nullable)
                    add("NOT NULL")
                if (column.unique)
                    add("UNIQUE")
            }.joinToString(" ")
        }
        val query = "CREATE TABLE IF NOT EXISTS ${table.tableName} $keys"
        return query
    }
}