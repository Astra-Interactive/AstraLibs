import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import ru.astrainteractive.astralibs.database_v2.*
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

    @BeforeTest
    fun setup() {
        databaseV2 = Database()
        runBlocking {
            databaseV2.openConnection("jdbc:sqlite:dbv2.db", "org.sqlite.JDBC")
        }
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
        assertDoesNotThrow { runBlocking { UserTable.create(databaseV2) } }
    }

    @Test
    fun `Insert statement query`() {
        assertDoesNotThrow { runBlocking { UserTable.create(databaseV2) } }
        val query = runBlocking {
            QueryGenerator.createInsertQuery(UserTable, InsertStatement(UserTable).apply {
                this[UserTable.name] = UUID.randomUUID().toString()
            })
        }
        assertEquals("INSERT INTO user_table (name) VALUES (?)", query)
    }

    @Test
    fun `Insert statement`() {
        assertDoesNotThrow { runBlocking { UserTable.create(databaseV2) } }
        assertDoesNotThrow {
            runBlocking {
                UserTable.insert(databaseV2) {
                    this[UserTable.name] = UUID.randomUUID().toString()
                }
            }
        }
    }

    @Test
    fun `Select statement`() {
        runBlocking {
            val users = UserTable.select<User>(databaseV2, "SELECT * FROM ${UserTable.tableName}") {
                User()
            }
            users.forEach {
                println(it)
                println(it.name)
                println(it.id)
            }
        }
    }
}

