package domain.dto

data class AuctionDTO(
    val id: Int,
    val discordId: String?,
    val minecraftUuid: String,
    val time: Long,
    val item: ByteArray,
    val price: Float,
    var expired: Boolean,
)
