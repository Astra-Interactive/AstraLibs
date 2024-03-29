package tests.mysql

import ORMTest
import domain.entities.SimpleUser
import domain.entities.SimpleUserTable
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class MySqlTest : ORMTest() {
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
        val id = SimpleUserTable.insert(database) {
            this[SimpleUserTable.name] = uuid
        }
        val user = SimpleUserTable.find(database, SimpleUser) {
            SimpleUserTable.id.eq(id)
        }.first()
        assertEquals(uuid, user.name)
    }
}
