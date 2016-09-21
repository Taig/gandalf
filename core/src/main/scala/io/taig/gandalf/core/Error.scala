package io.taig.gandalf.core

import cats.data.NonEmptyList

trait Error[-R <: Rule] {
    def show( arguments: R#Arguments ): NonEmptyList[String]
}

object Error {
    @inline
    def apply[R <: Rule](
        implicit
        e: Error[R]
    ): Error[R] = e

    def instance[R <: Rule](
        f: R#Arguments ⇒ NonEmptyList[String]
    ): Error[R] = new Error[R] {
        override def show( arguments: R#Arguments ) = f( arguments )
    }

    def one[R <: Rule](
        f: R#Arguments ⇒ String
    ): Error[R] = instance( arguments ⇒ NonEmptyList.of( f( arguments ) ) )

    def static[R <: Rule](
        value: String
    ): Error[R] = instance( _ ⇒ NonEmptyList.of( value ) )
}