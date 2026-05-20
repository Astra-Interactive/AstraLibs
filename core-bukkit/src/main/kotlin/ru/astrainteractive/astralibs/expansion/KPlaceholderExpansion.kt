package ru.astrainteractive.astralibs.expansion

import me.clip.placeholderapi.expansion.PlaceholderExpansion

/** Kotlin-friendly base class for PlaceholderAPI expansions that accepts metadata via constructor parameters. */
abstract class KPlaceholderExpansion(
    private val identifier: String,
    private val author: String,
    private val version: String
) : PlaceholderExpansion(), PlaceholderExpansionFacade {
    override fun getIdentifier(): String = identifier
    override fun getAuthor(): String = author
    override fun getVersion(): String = version
}
