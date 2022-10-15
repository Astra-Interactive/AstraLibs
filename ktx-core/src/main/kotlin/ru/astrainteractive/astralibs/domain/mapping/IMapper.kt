package ru.astrainteractive.astralibs.domain.mapping

const val NOT_EXISTS_ID = -1

/**
 * Basic mapper for DTO objects
 */
interface IMapper<I, O> {
    fun toDTO(it: I): O
    fun fromDTO(it: O): I
}