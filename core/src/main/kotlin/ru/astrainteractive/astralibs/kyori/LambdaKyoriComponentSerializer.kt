package ru.astrainteractive.astralibs.kyori

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.ComponentSerializer
import ru.astrainteractive.klibs.kstorage.api.CachedKrate

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

fun CachedKrate<KyoriComponentSerializer>.unwrap(): KyoriComponentSerializer {
    return LambdaKyoriComponentSerializer(getKyori = { this.cachedValue })
}
