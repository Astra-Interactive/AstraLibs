package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.InetAddress
import java.net.InetSocketAddress

object InetSocketAddressSerializer : KSerializer<InetSocketAddress> {
    @Serializable
    private data class Surrogate(
        @Serializable(InetAddressSerializer::class)
        @SerialName("inet_address")
        val address: InetAddress,
        @SerialName("port")
        val port: Int
    )

    override val descriptor = Surrogate
        .serializer()
        .descriptor

    override fun serialize(encoder: Encoder, value: InetSocketAddress) {
        val surrogate = Surrogate(
            address = value.address,
            port = value.port
        )

        encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): InetSocketAddress {
        val surrogate = decoder.decodeSerializableValue(Surrogate.serializer())
        return InetSocketAddress(surrogate.address, surrogate.port)
    }
}
