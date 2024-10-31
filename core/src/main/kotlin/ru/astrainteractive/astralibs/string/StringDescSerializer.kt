package ru.astrainteractive.astralibs.string

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class StringDescSerializer<T : StringDesc>(
    private val fromString: (String) -> T,
    private val toString: (T) -> String
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("StringDesc", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        val string = toString.invoke(value)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): T {
        val string = decoder.decodeString()
        val stringDesc = fromString.invoke(string)
        return stringDesc
    }
}

object RawStringDescSerializer : KSerializer<StringDesc.Raw> by StringDescSerializer(
    fromString = { string -> StringDesc.Raw(string) },
    toString = { stringDesc -> stringDesc.raw }
)

object PlainStringDescSerializer : KSerializer<StringDesc.Plain> by StringDescSerializer(
    fromString = { string -> StringDesc.Plain(string) },
    toString = { stringDesc -> stringDesc.raw }
)
