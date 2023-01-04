package ru.astrainteractive.astralibs.orm.query

import ru.astrainteractive.astralibs.orm.database.Table

class CreateQuery(val table: Table<*>) : Query {
    override fun generate(): String {
        val keys = table.columns.joinToString(",", "(", ")") { column ->
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