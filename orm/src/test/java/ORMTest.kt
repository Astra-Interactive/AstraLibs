import kotlinx.coroutines.runBlocking
import ru.astrainteractive.astralibs.orm.Database
import ru.astrainteractive.astralibs.orm.exception.DatabaseException
import kotlin.test.*

abstract class ORMTest {
    abstract val builder: () -> Database
    protected var database: Database? = null
    fun assertConnected(): Database {
        return database ?: throw DatabaseException.DatabaseNotConnectedException
    }

    @BeforeTest
    open fun setup(): Unit = runBlocking {
        database = builder()
        database?.openConnection()
    }

    @AfterTest
    fun destroy(): Unit = runBlocking {
        database?.closeConnection()
        database = null
    }

}