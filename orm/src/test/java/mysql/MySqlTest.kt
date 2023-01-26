package mysql

import ORMTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import rating_test.database.domain.entities.SimpleUser
import rating_test.database.domain.entities.SimpleUserTable
import ru.astrainteractive.astralibs.orm.DBConnection
import ru.astrainteractive.astralibs.orm.DBSyntax
import ru.astrainteractive.astralibs.orm.Database
import ru.astrainteractive.astralibs.orm.DefaultDatabase
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class MySqlTest : ORMTest() {
    private val dbconnection = DBConnection.MySQL(
        database = "XXXXXXXXXXXXXXX",
        ip = "XXXXXXXXXXXXXXX",
        port = 3306,
        username = "XXXXXXXXXXXXX",
        password = "XXXXXXXXXXXXXXX"
    )
    override val builder: () -> Database = {
        DefaultDatabase(dbconnection, DBSyntax.MySQL)
    }

    @Test
    fun `Create database table`(): Unit = runBlocking {
        val database = assertConnected()
        assertDoesNotThrow {
            runBlocking {
                SimpleUserTable.create(database)
                SimpleUserTable.drop(database)
            }
        }
    }
    @Test
    fun `Insert and select statement`(): Unit = runBlocking {
        val database = assertConnected()
        SimpleUserTable.create(database)
        val uuid = UUID.randomUUID().toString()
        val id = SimpleUserTable.insert(database){
            this[SimpleUserTable.name] = uuid
        }
        val user = SimpleUserTable.find(database,SimpleUser){
            SimpleUserTable.id.eq(id)
        }.first()
        assertEquals(uuid,user.name)
    }
}