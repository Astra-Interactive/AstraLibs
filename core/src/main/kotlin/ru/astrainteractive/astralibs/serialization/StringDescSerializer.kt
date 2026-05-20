package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.astrainteractive.astralibs.string.StringDesc

/**
 * [KSerializer] for [StringDesc] subtypes that serializes as a primitive string.
 *
 * @param fromString Converts a raw string to [T] on deserialization.
 * @param toString Extracts the raw string from [T] on serialization.
 */
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

/** A [KSerializer] for [StringDesc.Raw] that encodes and decodes the underlying raw string. */
object RawStringDescSerializer : KSerializer<StringDesc.Raw> by StringDescSerializer(
    fromString = { string -> StringDesc.Raw(string) },
    toString = { stringDesc -> stringDesc.raw }
)

/** A [KSerializer] for [StringDesc.Plain] that encodes and decodes the underlying plain string. */
object PlainStringDescSerializer : KSerializer<StringDesc.Plain> by StringDescSerializer(
    fromString = { string -> StringDesc.Plain(string) },
    toString = { stringDesc -> stringDesc.raw }
)

/**
 * A [KSerializer] for the [StringDesc] sealed interface that always deserializes to [StringDesc.Raw].
 * Used as the default serializer on the [StringDesc] type.
 */
object DefaultStringDescSerializer : KSerializer<StringDesc> by StringDescSerializer(
    fromString = { string -> StringDesc.Raw(string) },
    toString = { stringDesc -> stringDesc.raw }
)
