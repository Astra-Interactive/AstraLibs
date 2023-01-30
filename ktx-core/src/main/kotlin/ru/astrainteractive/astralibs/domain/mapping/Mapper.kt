package ru.astrainteractive.astralibs.domain.mapping

/**
 * Basic mapper for DTO objects
 */
interface Mapper<I, O> {
    fun toDTO(it: I): O
    fun fromDTO(it: O): I
}