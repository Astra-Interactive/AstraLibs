package rating_test.database

import kotlinx.coroutines.runBlocking
import rating_test.database.domain.DataSource
import rating_test.database.domain.IDataSource
import rating_test.database.domain.dto.AuctionDTO
import rating_test.database.domain.entities.AuctionTable
import ru.astrainteractive.astralibs.orm.DBConnection
import ru.astrainteractive.astralibs.orm.DBSyntax
import ru.astrainteractive.astralibs.orm.Database
import ru.astrainteractive.astralibs.orm.DefaultDatabase
import java.io.File
import java.util.*
import kotlin.random.Random
import kotlin.test.*

class AuctionsDomainTests {
    private lateinit var databaseV2: Database
    private lateinit var dataSource: IDataSource
    val randomAuction: AuctionDTO
        get() = AuctionDTO(
            id = -1,
            discordId = UUID.randomUUID().toString(),
            minecraftUuid = UUID.randomUUID().toString(),
            time = System.currentTimeMillis(),
            item = ByteArray(0),
            price = Random.nextInt().toFloat(),
            expired = false
        )

    @BeforeTest
    fun setup(): Unit = runBlocking {
        File("dbv2_auction.db").delete()
        databaseV2 = DefaultDatabase(DBConnection.SQLite("dbv2_auction.db"),DBSyntax.SQLite)
        databaseV2.openConnection()
        AuctionTable.create(databaseV2)
        dataSource = DataSource(databaseV2)
    }

    @AfterTest
    fun destruct(): Unit = runBlocking {
        databaseV2.closeConnection()
    }

    @Test
    fun `Insert, fetch, expire same auction`(): Unit = runBlocking {
        val auction = randomAuction
        // Insert and fetch
        val id = dataSource.insertAuction(auction)!!
        var auctionDTO = dataSource.fetchAuction(id)!!
        assertEquals(auctionDTO.minecraftUuid, auction.minecraftUuid)
        // Get unexpiredAuctions
        var amount = dataSource.getUserAuctions(auctionDTO.minecraftUuid, false)!!.size
        assertEquals(amount, 1)
        // Expire
        dataSource.expireAuction(auctionDTO)
        assertEquals(auctionDTO.expired, false)
        auctionDTO = dataSource.fetchAuction(id)!!
        assertEquals(auctionDTO.expired, true)
        // Get expiredAuctions
        amount = dataSource.getUserAuctions(auctionDTO.minecraftUuid, true)!!.size
        assertEquals(amount, 1)
        // Get unexpiredAuctions
        amount = dataSource.getUserAuctions(auctionDTO.minecraftUuid, false)!!.size
        assertEquals(amount, 0)
        // Count auctions
        amount = dataSource.countPlayerAuctions(auctionDTO.minecraftUuid)!!
        assertEquals(amount, 1)
        // Delete and count auction
        dataSource.deleteAuction(auctionDTO)
        amount = dataSource.countPlayerAuctions(auctionDTO.minecraftUuid)!!
        assertEquals(amount, 0)
        val oldAuctionDTO = randomAuction.copy(time = 0)
        dataSource.insertAuction(oldAuctionDTO)
        amount = dataSource.getAuctionsOlderThan(System.currentTimeMillis()-1).size
        assertEquals(amount,1)
    }

}