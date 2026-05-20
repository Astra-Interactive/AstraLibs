package ru.astrainteractive.astralibs.kyori

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.ComponentSerializer
import ru.astrainteractive.klibs.kstorage.api.CachedKrate

/**
 * A [KyoriComponentSerializer] that re-evaluates [getKyori] on every call, allowing the
 * active serializer to be swapped at runtime (e.g. stored in a [ru.astrainteractive.klibs.kstorage.api.CachedKrate]).
 *
 * @param getKyori Returns the current [KyoriComponentSerializer] to use.
 * @see unwrap
 */
class LambdaKyoriComponentSerializer(
    private val getKyori: () -> KyoriComponentSerializer
) : KyoriComponentSerializer {
    private val kyori: KyoriComponentSerializer
        get() = getKyori.invoke()

    override val type: KyoriComponentSerializerType
        get() = kyori.type

    override val serializer: ComponentSerializer<Component, out Component, String>
        get() = kyori.serializer

    override fun toComponent(string: String): Component = kyori.toComponent(string)
}

/** Wraps this [CachedKrate] as a [LambdaKyoriComponentSerializer] that always uses the current cached value. */
fun CachedKrate<KyoriComponentSerializer>.unwrap(): KyoriComponentSerializer {
    return LambdaKyoriComponentSerializer(getKyori = { this.cachedValue })
}
