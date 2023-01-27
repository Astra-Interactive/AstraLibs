package domain.dto

import domain.entities.Auction
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
        throw IllegalAccessError("Not implemented")
    }
}