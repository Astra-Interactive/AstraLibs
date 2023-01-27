import domain.DataSource
import domain.IDataSource
import domain.entities.*
import kotlinx.coroutines.runBlocking
import ru.astrainteractive.astralibs.orm.DBConnection
import ru.astrainteractive.astralibs.orm.Database
import ru.astrainteractive.astralibs.orm.exception.DatabaseException
import java.io.File
import kotlin.test.*

abstract class ORMTest(
    val builder: () -> Database = Resource::getDatabase
) {

    protected var database: Database? = null
    fun assertConnected(): Database {
        return database ?: throw DatabaseException.DatabaseNotConnectedException
    }

    @BeforeTest
    open fun setup(): Unit = runBlocking {
        database = builder()
        database?.openConnection()
        (database?.dbConnection as? DBConnection.MySQL)?.let {
            database?.let { AuctionTable.drop(it) }
            database?.let { SimpleUserTable.drop(it) }
            database?.let { UserTable.drop(it) }
            database?.let { UserRatingTable.drop(it) }
        }
    }

    @AfterTest
    fun destroy(): Unit = runBlocking {
        database?.closeConnection()
        (database?.dbConnection as? DBConnection.SQLite)?.let {
            File(it.dbName).delete()
        }
        database = null
    }

}