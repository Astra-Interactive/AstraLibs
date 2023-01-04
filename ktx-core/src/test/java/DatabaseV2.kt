import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import ru.astrainteractive.astralibs.database.sqlString
import ru.astrainteractive.astralibs.database_v2.*
import ru.astrainteractive.astralibs.database_v2.expression.Expression
import ru.astrainteractive.astralibs.database_v2.expression.SQLExpressionBuilder.eq
import java.util.UUID
import kotlin.test.*


object UserTable : Table<Int>("user_table") {
    override val id: Column<Int> = integer("id").primaryKey().autoIncrement()
    val name: Column<String> = text("name").unique()
}

class User : Entity<Int>(UserTable) {
    val id by UserTable.id
    var name by UserTable.name
}

class DatabaseV2 {
    private lateinit var databaseV2: Database
    val randomUser: User
        get() = runBlocking {
            val uuid = UUID.randomUUID().toString()
            val id = UserTable.insert() {
                this[UserTable.name] = uuid
            }
            return@runBlocking UserTable.find(constructor = ::User) {
                UserTable.id.eq(id)
            }.first()
        }

    @BeforeTest
    fun setup() {
        databaseV2 = Database()
        runBlocking {
            databaseV2.openConnection("jdbc:sqlite:dbv2.db", "org.sqlite.JDBC")
            UserTable.create()
        }
    }

    @AfterTest
    fun destruct() {
        runBlocking {
            databaseV2.closeConnection()
        }
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
        assertNotNull(databaseV2.connection)
        assertEquals(databaseV2.isConnected, true)
        runBlocking { databaseV2.closeConnection() }
        assertNull(databaseV2.connection)
        assertEquals(databaseV2.isConnected, false)
    }

    @Test
    fun `Create table`() {
        val expectedQuery =
            "CREATE TABLE IF NOT EXISTS user_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,name TEXT NOT NULL UNIQUE)"
        assertEquals(expectedQuery, QueryGenerator.createCreateStatement(UserTable))
        assertDoesNotThrow { runBlocking { UserTable.create() } }
    }

    @Test
    fun `Insert statement query`() {
        assertDoesNotThrow { runBlocking { UserTable.create() } }
        val query = runBlocking {
            QueryGenerator.createInsertQuery(UserTable, InsertStatement(UserTable).apply {
                this[UserTable.name] = UUID.randomUUID().toString()
            })
        }
        assertEquals("INSERT INTO user_table (name) VALUES (?)", query)
    }

    @Test
    fun `Insert statement`() {
        assertDoesNotThrow { runBlocking { UserTable.create() } }
        assertDoesNotThrow {
            runBlocking {
                UserTable.insert() {
                    this[UserTable.name] = UUID.randomUUID().toString()
                }
            }
        }
    }

    @Test
    fun `Select statement`() {
        runBlocking {
            val user = randomUser
            val selectedById = UserTable.find(constructor = ::User) {
                UserTable.id.eq(user.id)
            }.firstOrNull()
            assertEquals(user.name, selectedById?.name)
            // Select by ID
            val byID = UserTable.find(constructor = ::User) {
                UserTable.id.eq(user.id)
            }.firstOrNull()
            assertEquals(byID?.name, selectedById?.name)
        }
    }

    @Test
    fun `Update statement`() {
        runBlocking {
            val newUUID = UUID.randomUUID().toString()
            val user = randomUser.apply { name = newUUID }
            UserTable.update(entity = user)
            val updated = UserTable.find(constructor = ::User) {
                UserTable.id.eq(user.id)
            }.first()
            assertEquals(updated.name, newUUID)
        }
    }


    @Test
    fun `Delete statement`() {
        runBlocking {
            // Delete by id
            var user = randomUser
            UserTable.delete<User> {
                UserTable.id.eq(user.id)
            }
            var selected = UserTable.find(constructor = ::User) {
                UserTable.id.eq(user.id)
            }.firstOrNull()
            assert(selected == null)
            // Delete by name
            user = randomUser
            UserTable.delete<User> {
                UserTable.name.eq(user.name)
            }
            selected = UserTable.find(constructor = ::User) {
                UserTable.name.eq(user.name)
            }.firstOrNull()
            assert(selected == null)
            // Delete by name and id
            user = randomUser
            UserTable.delete<User> {
                UserTable.name.eq(user.name).and(UserTable.id.eq(user.id))
            }
            selected = UserTable.find(constructor = ::User) {
                UserTable.name.eq(user.name).and(UserTable.id.eq(user.id))
            }.firstOrNull()
            assert(selected == null)
        }
    }

    @Test
    fun `Check expression builder`() {
        val columnName = UserTable.name.name
        var ep = UserTable.name.eq("SomeEq")
        var query = "$columnName = \"SomeEq\""
        assertEquals(ep.toString(), query)
        query = "$columnName = \"1\" AND ($columnName = \"2\")"
        ep = UserTable.name.eq("1").and(UserTable.name.eq("2"))
        assertEquals(ep.toString(), query)
    }

    @Test
    fun `Find`() {
        runBlocking {
            val uuid = UUID.randomUUID().toString()
            val id = UserTable.insert() {
                this[UserTable.name] = uuid
            }

            val user: User? = UserTable.find(constructor = ::User) {
                UserTable.id.eq(id)
                    .and(UserTable.name.eq("$uuid"))
            }.firstOrNull()
            assertEquals(user?.id, id)
            assertEquals(user?.name, uuid)
        }
    }
}


