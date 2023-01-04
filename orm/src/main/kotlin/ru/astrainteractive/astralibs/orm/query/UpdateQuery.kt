package ru.astrainteractive.astralibs.orm.query

import ru.astrainteractive.astralibs.orm.database.Entity

class UpdateQuery(val entity: Entity<*>) : Query {
    override fun generate(): String {
        val tableName = entity.table.tableName
        val primaryKey = entity.table.id
        val primaryKeyValue = entity[primaryKey]
        val columns = entity.table.columns.filter { !it.primaryKey }
        val keys = columns.joinToString(",") {
            "${it.name}=?"
        }
        val query =
            "UPDATE $tableName SET $keys WHERE ${primaryKey.name}=${primaryKeyValue}"
        return query
    }
}