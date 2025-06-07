package ru.astrainteractive.astralibs.exposed.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

/**
 * Represents a database table with a string-based ID column.
 *
 * This class extends [IdTable] to provide a table structure where the primary key is of type [String].
 * It is useful for defining tables where the ID is represented as a string, such as UUIDs or other
 * string identifiers.
 *
 * @param name The name of the table. If not specified, it defaults to an empty string.
 * @param columnName The name of the ID column. If not specified, it defaults to "id".
 *
 * @property id The column representing the string-based ID, which is also the primary key of the table.
 * @property primaryKey The primary key of the table, which is based on the string ID column.
 */
open class StringIdTable(name: String = "", columnName: String = "id") : IdTable<String>(name) {

    /**
     * The column representing the string-based ID.
     *
     * This column is defined as a text column and is used as the primary key for the table.
     */
    final override val id: Column<EntityID<String>> = text(columnName).entityId()

    /**
     * The primary key of the table.
     *
     * The primary key is based on the string ID column, ensuring that each entry in the table
     * can be uniquely identified by its string ID.
     */
    final override val primaryKey = PrimaryKey(id)
}
