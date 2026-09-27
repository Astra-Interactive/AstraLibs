package ru.astrainteractive.astralibs.localization.component

import net.kyori.adventure.text.Component

data class PlaceholderReplacement(
    val placeholder: String,
    val value: LocalizableComponent
) {
    companion object {
        /** Inserts [text] as plain text: its `&` codes and tags stay literal, so player input is safe here. */
        fun plain(placeholder: String, text: String): PlaceholderReplacement {
            return PlaceholderReplacement(
                placeholder = placeholder,
                value = ConstantLocalizableComponent(Component.text(text))
            )
        }
    }
}
