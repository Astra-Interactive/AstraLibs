package tests

import ORMTest
import kotlinx.coroutines.runBlocking
import domain.entities.User
import domain.entities.UserRatingTable
import domain.entities.UserTable
import ru.astrainteractive.astralibs.orm.DBConnection
import ru.astrainteractive.astralibs.orm.DBSyntax
import ru.astrainteractive.astralibs.orm.Database
import ru.astrainteractive.astralibs.orm.DefaultDatabase
import java.util.*
import kotlin.test.*


class DatabaseTests : ORMTest() {

    val randomUser: User
        get() = runBlocking {
            val database = assertConnected()
            val uuid = UUID.randomUUID().toString()
            val id = UserTable.insert(database) {
                this[UserTable.uuid] = uuid
                this[UserTable.lastUpdated] = System.currentTimeMillis()
            }
            return@runBlocking UserTable.find(database, constructor = User) {
                UserTable.id.eq(id)
            }.first()
        }

    @BeforeTest
    override fun setup(): Unit = runBlocking {
        super.setup()
        val database = assertConnected()
        UserTable.create(database)
        UserRatingTable.create(database)
    }

    @Test
    fun `Test more expression`() {
        val database = assertConnected()
        val timeMillis = System.currentTimeMillis()
        randomUser
        val users = UserTable.find(database, constructor = User) {
            UserTable.lastUpdated.more(timeMillis - 100)
        }
        assertEquals(1, users.size)

        randomUser

    }
}