package ru.astrainteractive.astralibs.expansion

import me.clip.placeholderapi.expansion.PlaceholderExpansion

abstract class KPlaceholderExpansion(
    private val identifier: String,
    private val author: String,
    private val version: String
) : PlaceholderExpansion(), PlaceholderExpansionApi {
    override fun getIdentifier(): String = identifier
    override fun getAuthor(): String = author
    override fun getVersion(): String = version
}
