package ru.astrainteractive.astralibs.orm.query

import ru.astrainteractive.astralibs.orm.DBSyntax
import ru.astrainteractive.astralibs.orm.database.Table

class CreateQuery(val table: Table<*>,val syntax: DBSyntax) : Query {
    override fun generate(): String {
        val keys = table.columns.joinToString(",", "(", ")") { column ->
            buildList<String> {
                add("${column.name} ${column.type}")
                if (column.primaryKey) {
                    add(syntax.PRIMARY_KEY)
                    if (column.autoIncrement)
                        add(syntax.AUTO_INCREMENT)
                }
                if (!column.nullable)
                    add(syntax.NOT_NULL)
                if (column.unique)
                    add("UNIQUE")
            }.joinToString(" ")
        }
        val query = "CREATE TABLE IF NOT EXISTS ${table.tableName} $keys"
        return query
    }
}