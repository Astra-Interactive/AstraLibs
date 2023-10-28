package ru.astrainteractive.astralibs.string

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import ru.astrainteractive.astralibs.serialization.KyoriComponentSerializer
import ru.astrainteractive.klibs.kdi.Provider
import ru.astrainteractive.klibs.kdi.getValue

interface BukkitTranslationContext {

    fun Audience.sendMessage(stringDesc: StringDesc)
    fun StringDesc.toComponent(): Component

    class Default(
        kyoriComponentSerializerProvider: Provider<KyoriComponentSerializer>
    ) : BukkitTranslationContext {
        private val kyoriComponentSerializer by kyoriComponentSerializerProvider

        override fun Audience.sendMessage(stringDesc: StringDesc) {
            val component = kyoriComponentSerializer.toComponent(stringDesc)
            sendMessage(component)
        }

        override fun StringDesc.toComponent(): Component {
            return kyoriComponentSerializer.toComponent(this)
        }
    }
}
