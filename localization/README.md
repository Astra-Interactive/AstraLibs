# Module localization

Per-player texts for Minecraft plugins and mods. A text keeps every translation it has and becomes an Adventure `Component` only when the language of its receiver is known, so one `sendMessage` shows Russian to a player with a Russian client and English to everyone else.

The module also holds the markup parsers the rest of AstraLibs uses: MiniMessage, legacy `&` codes, JSON, and both markups mixed in one string.

## Installation

`core` exposes this module as an `api` dependency, so a plugin that depends on `core` already has it. Add it on its own only where `core` is not needed:

```kotlin
dependencies {
    implementation("ru.astrainteractive.astralibs:localization:<version>")
}
```

Adventure and kaml are `compileOnly`: the server or the plugin jar has to provide them at runtime.

| Library                                                             | Needed for                                                           |
|---------------------------------------------------------------------|----------------------------------------------------------------------|
| `adventure-api`                                                     | everything                                                           |
| `adventure-text-minimessage`, `adventure-text-serializer-legacy`    | rendering any `LocalizedText`                                        |
| `kaml`                                                              | `LocalizedTextSerializer`, even when the file being read is not YAML |
| `adventure-text-serializer-gson`, `adventure-text-serializer-plain` | only `KyoriComponentSerializer.Json`, `.Gson` and `.Plain`           |

Paper ships Adventure together with MiniMessage. Forge and NeoForge jars have to bundle MiniMessage themselves, and every plugin bundles kaml.

## Sending a text

```kotlin
val greeting = LocalizedText.build {
    translation(MinecraftLocales.RU_RU, "&aПривет, %player%!")
    translation(MinecraftLocales.EN_US, "<green>Hello, %player%!")
}

player.sendMessage(greeting.replace("%player%", player.name))
```

`sendMessage(LocalizableComponent)` is a member of `KAudience` from `core`: it renders the text in the receiver's `locale` and sends it. `Iterable<KAudience>.sendMessage(text)` renders it once per receiver, so a broadcast reaches each player in their own language.

| Receiver                                                                                                       | `locale`                                       |
|----------------------------------------------------------------------------------------------------------------|------------------------------------------------|
| Paper: `OnlineKPlayer`, `Audience.asKAudience()`                                                               | the client language Paper reports              |
| Forge / NeoForge: `OnlineKPlayer`, `ServerPlayer.asKAudience()`, a player's `CommandSourceStack.asKAudience()` | the client language from `clientInformation()` |
| Console, RCON, a command source without a player                                                               | `Locale.ROOT`                                  |

On Forge and NeoForge a player's `locale` is also `Locale.ROOT` until the client has sent its settings.

## Choosing the translation

`LocalizedText.toComponent(locale)` takes the first text that exists:

1. the translation for exactly this locale;
2. a translation for the same language, so `en_gb` reads `en_us`;
3. the shared text: `"*"` in a file, `shared(...)` in code;
4. the first translation listed, which makes it the default language of the text;
5. nothing at all renders `Component.empty()`.

The console and languages nobody wrote a translation for end at step 3 or 4.

## Texts in configuration files

Declare every text as a `LocalizedText` with a default, so a missing key keeps working:

```kotlin
@Serializable
data class PluginTranslation(
    @SerialName("reload_completed")
    val reloadCompleted: LocalizedText = LocalizedText.build {
        translation(MinecraftLocales.RU_RU, "&aПерезагрузка завершена")
        translation(MinecraftLocales.EN_US, "&aReload complete")
    },
    @SerialName("prefix")
    val prefix: LocalizedText = LocalizedText.shared("&7[Plugin] ")
)
```

In YAML a text is either one string for every language or a map of Minecraft language codes:

```yaml
prefix: "&7[Plugin] "
reload_completed:
  ru_ru: "&aПерезагрузка завершена"
  en_us: "<green>Reload complete"
  "*": "&aReload complete"
```

- `"*"` holds the shared text. It must be quoted: a bare `*` is a YAML alias.
- Without `"*"`, the language listed first is the default one.
- `null` and an empty map are an empty text.
- Only YAML read by kaml can hold the map form; other formats read the string form.

A configuration field that names a language uses `@Serializable(with = MinecraftLocaleSerializer::class) val locale: Locale`. A blank code fails the whole file, so a typo shows up in the load log. `MinecraftLocaleSerializer.parse(code)` reads codes the way clients send them, including `enws` without a country.

## Placeholders

Placeholders are replaced after the markup is parsed, in the receiver's language:

```kotlin
fun itemAdded(amount: Int, item: String): LocalizableComponent = itemAdded.replaceAll(
    PlaceholderReplacement.plain("%amount%", amount.toString()),
    PlaceholderReplacement.plain("%item%", item)
)
```

| Call                                                | Inserts                                                                     |
|-----------------------------------------------------|-----------------------------------------------------------------------------|
| `replace(placeholder, text: String)`                | plain text: `&` codes and tags in it stay literal, so player input is safe  |
| `replace(placeholder, value: LocalizableComponent)` | another text, rendered in the same language                                 |
| `replaceAll(vararg PlaceholderReplacement)`         | several values in one pass: no value is searched for the other placeholders |

Use `replaceAll` whenever more than one value comes from players; chained `replace` calls search what earlier calls inserted, like chained `String.replace`. Placeholders inside hover texts are replaced, those inside click actions are not, and a placeholder split by formatting (`%pla&ayer%`) is not found.

## Joining texts

| Call                                 | Result                                                                                                  |
|--------------------------------------|---------------------------------------------------------------------------------------------------------|
| `LocalizedText.concat(other)`        | joins the markup strings, so a `&` code of the first text keeps applying; the usual way to add a prefix |
| `LocalizableComponent + other`       | keeps both parts as siblings, so the style of the first does not leak into the second                   |
| `Component.asLocalizableComponent()` | a ready component that looks the same in every language                                                 |

`concat` keeps a language only when both texts can supply it; any other language reads the default language of the joined text instead of mixing two languages in one line.

## Texts built in code

`LocalizableComponent` is a `fun interface`, so a text that depends on the language needs no class of its own:

```kotlin
val onlineCount = LocalizableComponent { locale ->
    Component.text(NumberFormat.getIntegerInstance(locale).format(count))
}
```

## Markup

`LocalizedText` always parses with `AutoComponentSerializer`: MiniMessage tags first, then legacy `&` codes inside each text part.

- JSON is never parsed, so player text like `{text:hi}` cannot turn into a component.
- A string MiniMessage rejects, such as one with `§` codes, is parsed as legacy only.
- `&` codes stay literal inside `<gradient>`, `<rainbow>` and `<pride>`, in `<lang>` arguments and in entity hover names; `&r` does not end an enclosing tag.
- Escape player text with `MiniMessage.escapeTags` before building a markup string from it. `replace` needs no escaping.

Legacy and auto-parsed text is upright by default, although item names and lore are otherwise italic.

`KyoriComponentSerializer` gives the parsers one interface. `KyoriComponentSerializerType` is `@Serializable`, so a configuration can choose one, and `KyoriComponentSerializer.ofType(type)` returns it.

| Type          | Parses                                |
|---------------|---------------------------------------|
| `Auto`        | MiniMessage tags and legacy `&` codes |
| `MiniMessage` | MiniMessage tags                      |
| `Legacy`      | `&` codes                             |
| `Json`        | JSON components                       |
| `Gson`        | JSON components through Gson          |
| `Plain`       | plain text, no formatting             |

## Layout

| Package     | Contents                                                           |
|-------------|--------------------------------------------------------------------|
| `component` | `LocalizableComponent`, placeholders, joining, `Component` helpers |
| `locale`    | `MinecraftLocales`, `MinecraftLocaleSerializer`                    |
| `markup`    | `KyoriComponentSerializer`, `AutoComponentSerializer`              |
| `text`      | `LocalizedText`, its builder and its YAML serializer               |
