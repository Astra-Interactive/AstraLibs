package ru.astrainteractive.astralibs.orm.query

import ru.astrainteractive.astralibs.orm.database.Column
import ru.astrainteractive.astralibs.orm.database.Table
import ru.astrainteractive.astralibs.orm.expression.Expression
import ru.astrainteractive.astralibs.orm.expression.SQLExpressionBuilder

class SelectQuery(
    val table: Table<*>,
    vararg val columns: Column<*> = table.columns.toTypedArray(),
    val expression: SQLExpressionBuilder.() -> Expression<Boolean> = {Expression.Empty }
) : Query {
    override fun generate(): String {
        val op: Expression<Boolean> = SQLExpressionBuilder.expression()
        val selection = if (columns.size == table.columns.size) "*"
        else columns.joinToString(",","(", ")") { it.name }
        var query = "SELECT $selection FROM ${table.tableName}"
        if (op.toString().isNotEmpty())
            query = "$query WHERE $op"
        return query
    }
}

