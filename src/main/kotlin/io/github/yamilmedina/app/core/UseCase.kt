package io.github.yamilmedina.app.core

/**
 * Use cases contract.
 */
interface UseCase<in Input, out Output> {
    operator fun invoke(input: Input): Output
}