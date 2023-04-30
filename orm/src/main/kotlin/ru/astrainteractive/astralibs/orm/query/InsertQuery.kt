package ru.astrainteractive.astralibs.orm.query

import ru.astrainteractive.astralibs.orm.database.Table
import ru.astrainteractive.astralibs.orm.statement.DBStatement

class InsertQuery(private val table: Table<*>, private val statement: DBStatement) : Query {
    override fun generate(): String {
        val statementValues = statement.values
        val keys = statementValues.keys.joinToString(", ", "(", ")") { it.name }
        val valuesQuery = statementValues.values.joinToString(", ", "(", ")") { "?" }
        val query = "INSERT INTO ${table.tableName} $keys VALUES $valuesQuery"
        return query
    }
}
