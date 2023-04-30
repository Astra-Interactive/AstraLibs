package tests

import ORMTest
import domain.entities.SimpleUser
import domain.entities.SimpleUserTable
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import ru.astrainteractive.astralibs.orm.expression.SQLExpressionBuilder.eq
import ru.astrainteractive.astralibs.orm.query.CreateQuery
import ru.astrainteractive.astralibs.orm.query.InsertQuery
import ru.astrainteractive.astralibs.orm.statement.DBStatement
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DatabaseV2 : ORMTest() {
    val randomUser: SimpleUser
        get() = runBlocking {
            val database = assertConnected()
            val uuid = UUID.randomUUID().toString()
            val id = SimpleUserTable.insert(database) {
                this[SimpleUserTable.name] = uuid
            }
            return@runBlocking SimpleUserTable.find(database, constructor = SimpleUser) {
                SimpleUserTable.id.eq(id)
            }.first()
        }

    @BeforeTest
    override fun setup(): Unit = runBlocking {
        super.setup()
        val database = assertConnected()
        SimpleUserTable.create(database)
    }

    @Test
    fun `Test randomUser`() {
        assertDoesNotThrow {
            randomUser
        }
        assertNotNull(randomUser)
    }

    @Test
    fun `Test database connection`() {
        val database = assertConnected()
        assertNotNull(database.connection)
        assertEquals(database.isConnected, true)
        runBlocking { database.closeConnection() }
        assertNull(database.connection)
        assertEquals(database.isConnected, false)
    }

    @Test
    fun `Create table`() {
        val database = assertConnected()
        val syntax = database.dbSyntax
        val expectedQuery = """
            CREATE TABLE IF NOT EXISTS user_table (id INTEGER ${syntax.PRIMARY_KEY} ${syntax.AUTO_INCREMENT} ${syntax.NOT_NULL},name VARCHAR(128) ${syntax.NOT_NULL} UNIQUE)
        """.trimIndent()
        assertEquals(expectedQuery, CreateQuery(SimpleUserTable, database.dbSyntax).generate())
        assertDoesNotThrow { runBlocking { SimpleUserTable.create(database) } }
    }

    @Test
    fun `Insert statement query`() {
        val database = assertConnected()
        assertDoesNotThrow { runBlocking { SimpleUserTable.create(database) } }
        val query = runBlocking {
            InsertQuery(
                SimpleUserTable,
                DBStatement().apply {
                    this[SimpleUserTable.name] = UUID.randomUUID().toString()
                }
            ).generate()
        }
        assertEquals("INSERT INTO user_table (name) VALUES (?)", query)
    }

    @Test
    fun `Insert statement`() {
        val database = assertConnected()
        assertDoesNotThrow { runBlocking { SimpleUserTable.create(database) } }
        assertDoesNotThrow {
            runBlocking {
                SimpleUserTable.insert(database) {
                    this[SimpleUserTable.name] = UUID.randomUUID().toString()
                }
            }
        }
    }

    @Test
    fun `Select statement`() {
        val database = assertConnected()
        runBlocking {
            val user = randomUser
            val selectedById = SimpleUserTable.find(database, constructor = SimpleUser) {
                SimpleUserTable.id.eq(user.id)
            }.firstOrNull()
            assertEquals(user.name, selectedById?.name)
            // Select by ID
            val byID = SimpleUserTable.find(database, constructor = SimpleUser) {
                SimpleUserTable.id.eq(user.id)
            }.firstOrNull()
            assertEquals(byID?.name, selectedById?.name)
        }
    }

    @Test
    fun `Update statement`() {
        val database = assertConnected()
        runBlocking {
            val newUUID = UUID.randomUUID().toString()
            val user = randomUser.apply { name = newUUID }
            SimpleUserTable.update(database, entity = user)
            val updated = SimpleUserTable.find(database, constructor = SimpleUser) {
                SimpleUserTable.id.eq(user.id)
            }.first()
            assertEquals(updated.name, newUUID)
        }
    }

    @Test
    fun `Delete statement`() {
        val database = assertConnected()
        runBlocking {
            // Delete by id
            var user = randomUser
            SimpleUserTable.delete<SimpleUser>(database) {
                SimpleUserTable.id.eq(user.id)
            }
            var selected = SimpleUserTable.find(database, constructor = SimpleUser) {
                SimpleUserTable.id.eq(user.id)
            }.firstOrNull()
            assert(selected == null)
            // Delete by name
            user = randomUser
            SimpleUserTable.delete<SimpleUser>(database) {
                SimpleUserTable.name.eq(user.name)
            }
            selected = SimpleUserTable.find(database, constructor = SimpleUser) {
                SimpleUserTable.name.eq(user.name)
            }.firstOrNull()
            assert(selected == null)
            // Delete by name and id
            user = randomUser
            SimpleUserTable.delete<SimpleUser>(database) {
                SimpleUserTable.name.eq(user.name).and(SimpleUserTable.id.eq(user.id))
            }
            selected = SimpleUserTable.find(database, constructor = SimpleUser) {
                SimpleUserTable.name.eq(user.name).and(SimpleUserTable.id.eq(user.id))
            }.firstOrNull()
            assert(selected == null)
        }
    }

    @Test
    fun `Check expression builder`() {
        val columnName = SimpleUserTable.name.name
        var ep = SimpleUserTable.name.eq("SomeEq")
        var query = "$columnName = \"SomeEq\""
        assertEquals(ep.toString(), query)
        query = "$columnName = \"1\" AND ($columnName = \"2\")"
        ep = SimpleUserTable.name.eq("1").and(SimpleUserTable.name.eq("2"))
        assertEquals(ep.toString(), query)
    }

    @Test
    fun `Find`() {
        val database = assertConnected()
        runBlocking {
            val uuid = UUID.randomUUID().toString()
            val id = SimpleUserTable.insert(database) {
                this[SimpleUserTable.name] = uuid
            }

            val user: SimpleUser? = SimpleUserTable.find(database, constructor = SimpleUser) {
                SimpleUserTable.id.eq(id)
                    .and(SimpleUserTable.name.eq("$uuid"))
            }.firstOrNull()
            assertEquals(user?.id, id)
            assertEquals(user?.name, uuid)
        }
    }
}
