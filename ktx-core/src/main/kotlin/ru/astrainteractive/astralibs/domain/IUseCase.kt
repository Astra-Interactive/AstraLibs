package ru.astrainteractive.astralibs.domain

/**
 * Basic UseCase for your needs
 * Consider returning always non-nullable type and throw exceptions instead
 */
interface IUseCase<out Type, in Params> {
    suspend fun run(params: Params): Type
    suspend operator fun invoke(params: Params) = run(params)
}