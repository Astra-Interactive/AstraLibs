package ru.astrainteractive.astralibs.string

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object StringDescSerializer : KSerializer<StringDesc.Raw> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("StringDesc", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: StringDesc.Raw) {
        val string = value.raw
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): StringDesc.Raw {
        val string = decoder.decodeString()
        return StringDesc.Raw(string)
    }
}
