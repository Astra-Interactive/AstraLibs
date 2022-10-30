package rating_test.database.domain.dto

import rating_test.database.domain.entities.Auction
import rating_test.database.domain.entities.AuctionTable
import ru.astrainteractive.astralibs.domain.mapping.IMapper

object AuctionMapper : IMapper<Auction, AuctionDTO> {

    override fun toDTO(it: Auction): AuctionDTO = AuctionDTO(
        id = it.id,
        discordId = it.discordId,
        minecraftUuid = it.minecraftUuid,
        time = it.time,
        item = it.item,
        price = it.price,
        expired = it.expired==1
    )

    override fun fromDTO(it: AuctionDTO): Auction {
        return AuctionTable.find(constructor = ::Auction) {
            AuctionTable.id.eq(it.id)
        }.first()
    }
}