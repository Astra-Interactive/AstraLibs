package ru.astrainteractive.astralibs.localization.text

import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.YamlNull
import com.charleskorn.kaml.YamlScalar
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.astrainteractive.astralibs.localization.locale.MinecraftLocaleSerializer

/**
 * Reads a [LocalizedText] written either as one string shared by every language or as a map of Minecraft
 * language codes to texts, where `"*"` (quoted, since a bare `*` is a YAML alias) holds the shared text:
 *
 * ```yaml
 * prefix: "&7[TPA] "
 * request_sent:
 *   ru_ru: "&aЗапрос отправлен"
 *   en_us: "<green>Request sent"
 *   "*": "&aRequest sent"
 * ```
 *
 * Without `"*"`, the language listed first is the default one: languages without a translation read it.
 * A blank key also means the shared text; `null` and an empty map mean an empty text. Only YAML read by kaml
 * can hold the map form; any other format reads the string form. kaml has to be on the runtime classpath either
 * way, because the decoder is checked against its type.
 */
object LocalizedTextSerializer : KSerializer<LocalizedText> {
    private const val SHARED_KEY = "*"

    private val textByKeySerializer = MapSerializer(String.serializer(), String.serializer())

    /** Contextual kind makes kaml hand over the raw node instead of rejecting a map where a string is declared. */
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor(
        serialName = "ru.astrainteractive.astralibs.localization.text.LocalizedText",
        kind = SerialKind.CONTEXTUAL
    )

    private fun YamlNode.location(): String = path.toHumanReadableString()

    private fun textOf(value: YamlNode): String {
        if (value !is YamlScalar) {
            throw SerializationException("Expected a text at ${value.location()}, but got ${value.contentToString()}")
        }
        return value.content
    }

    private fun decodeTranslations(node: YamlMap): LocalizedText {
        val builder = LocalizedTextBuilder()
        node.entries.forEach { (key, value) ->
            val text = textOf(value)
            val locale = key.content
                .takeIf { code -> code != SHARED_KEY }
                ?.let(MinecraftLocaleSerializer::parse)
            if (locale == null) builder.shared(text) else builder.translation(locale, text)
        }
        return builder.build()
    }

    private fun decodeYaml(node: YamlNode): LocalizedText {
        return when (node) {
            is YamlScalar -> LocalizedText.shared(node.content)
            is YamlMap -> decodeTranslations(node)
            is YamlNull -> LocalizedText(sharedText = null, translationByLocale = emptyMap())
            else -> throw SerializationException(
                "Expected a text or a map of language codes to texts at ${node.location()}, " +
                    "but got ${node.contentToString()}"
            )
        }
    }

    override fun deserialize(decoder: Decoder): LocalizedText {
        if (decoder !is YamlInput) return LocalizedText.shared(decoder.decodeString())
        return decodeYaml(decoder.node)
    }

    override fun serialize(encoder: Encoder, value: LocalizedText) {
        val sharedText = value.sharedText
        if (sharedText != null && value.translationByLocale.isEmpty()) {
            encoder.encodeString(sharedText)
            return
        }
        val textByKey = buildMap {
            value.translationByLocale.forEach { (locale, text) -> put(MinecraftLocaleSerializer.format(locale), text) }
            if (sharedText != null) put(SHARED_KEY, sharedText)
        }
        encoder.encodeSerializableValue(textByKeySerializer, textByKey)
    }
}
