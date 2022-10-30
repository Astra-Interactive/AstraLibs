package rating_test.database

import kotlinx.coroutines.runBlocking
import rating_test.database.entities.User
import rating_test.database.entities.UserRatingTable
import rating_test.database.entities.UserTable
import ru.astrainteractive.astralibs.database_v2.Database
import java.util.*
import kotlin.test.*

class DatabaseTests {
    private lateinit var databaseV2: Database
    val randomUser: User
        get() = runBlocking {
            val uuid = UUID.randomUUID().toString()
            val id = UserTable.insert() {
                this[UserTable.uuid] = uuid
                this[UserTable.lastUpdated] = System.currentTimeMillis()
            }
            return@runBlocking UserTable.find(constructor = ::User) {
                UserTable.id.eq(id)
            }.first()
        }

    @BeforeTest
    fun setup(): Unit = runBlocking {
        databaseV2 = Database()
        databaseV2.openConnection("jdbc:sqlite:dbv2_2.db", "org.sqlite.JDBC")
        UserTable.create()
        UserRatingTable.create()
    }

    @AfterTest
    fun destruct(): Unit = runBlocking {
        databaseV2.closeConnection()
    }

    @Test
    fun `Test more expression`(){
        val timeMillis = System.currentTimeMillis()
        randomUser
        val users = UserTable.find(constructor = ::User){
            UserTable.lastUpdated.more(timeMillis)
        }
        assertEquals(1,users.size)

        randomUser

    }
}