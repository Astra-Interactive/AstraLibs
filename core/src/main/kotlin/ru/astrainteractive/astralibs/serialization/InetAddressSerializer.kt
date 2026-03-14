package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.InetAddress

object InetAddressSerializer : KSerializer<InetAddress> {
    override val descriptor = PrimitiveSerialDescriptor(
        serialName = "InetAddress",
        kind = PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: InetAddress) {
        encoder.encodeString(value.hostAddress)
    }

    override fun deserialize(decoder: Decoder): InetAddress {
        val value = decoder.decodeString()
        return InetAddress.getByName(value)
    }
}
