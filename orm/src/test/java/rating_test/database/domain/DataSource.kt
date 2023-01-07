package rating_test.database.domain

import rating_test.database.domain.dto.AuctionDTO
import rating_test.database.domain.dto.AuctionMapper
import rating_test.database.domain.entities.Auction
import rating_test.database.domain.entities.AuctionTable
import ru.astrainteractive.astralibs.orm.Database

interface IDataSource {
    suspend fun insertAuction(auctionDTO: AuctionDTO): Long?
    suspend fun expireAuction(auctionDTO: AuctionDTO)
    suspend fun getUserAuctions(uuid: String, expired: Boolean): List<AuctionDTO>
    suspend fun getAuctionsOlderThan(millis: Long): List<AuctionDTO>
    suspend fun fetchAuction(id: Long): AuctionDTO?
    suspend fun deleteAuction(auction: AuctionDTO)
    suspend fun countPlayerAuctions(uuid: String): Int?
}

class DataSource(private val database: Database) : IDataSource {
    override suspend fun insertAuction(auctionDTO: AuctionDTO): Long {
        return AuctionTable.insert(database) {
            this[AuctionTable.discordId] = auctionDTO.discordId
            this[AuctionTable.minecraftUuid] = auctionDTO.minecraftUuid
            this[AuctionTable.time] = auctionDTO.time
            this[AuctionTable.item] = auctionDTO.item
            this[AuctionTable.price] = auctionDTO.price
            this[AuctionTable.expired] = if (auctionDTO.expired) 1 else 0
        }.toLong()
    }

    override suspend fun expireAuction(auctionDTO: AuctionDTO) {
        AuctionTable.find(database,constructor = Auction) {
            AuctionTable.id.eq(auctionDTO.id)
        }?.firstOrNull()?.let {
            it.expired = 1
            AuctionTable.update(database,entity = it)
        }
    }

    override suspend fun getUserAuctions(uuid: String, expired: Boolean): List<AuctionDTO> {
        return AuctionTable.find(database,constructor = Auction) {
            AuctionTable.minecraftUuid.eq(uuid).and(
                AuctionTable.expired.eq(if (expired) 1 else 0)
            )
        }.map(AuctionMapper::toDTO)
    }

    override suspend fun getAuctionsOlderThan(millis: Long): List<AuctionDTO> {
        val currentTime = System.currentTimeMillis()
        val time = currentTime - millis
        return AuctionTable.find(database,constructor = Auction) {
            AuctionTable.time.less(time)
        }.map(AuctionMapper::toDTO)
    }

    override suspend fun fetchAuction(id: Long): AuctionDTO? {
        return AuctionTable.find(database,constructor = Auction) {
            AuctionTable.id.eq(id)
        }.map(AuctionMapper::toDTO)?.firstOrNull()
    }

    override suspend fun deleteAuction(auction: AuctionDTO) {
        AuctionTable.delete<Auction>(database) {
            AuctionTable.id.eq(auction.id)
        }
    }

    override suspend fun countPlayerAuctions(uuid: String): Int {
        return AuctionTable.count(database) {
            AuctionTable.minecraftUuid.eq(uuid)
        }
    }
}