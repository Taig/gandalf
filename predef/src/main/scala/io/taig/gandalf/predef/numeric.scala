package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Arguments, Condition }
import io.taig.gandalf.core.Rule.Applyable

import scala.Ordering.Implicits._

trait numeric {
    def zero[T]( implicit n: Numeric[T] ): T = n.zero

    class gt[T <: U: ValueOf, U: Numeric]
            extends Condition.With[U]( _ > valueOf[T] )
            with Arguments.With[T] {
        override val argument = valueOf[T]
    }

    object gt {
        def apply[T: Numeric]( value: T ): gt[value.type, T] = new gt[value.type, T]

        implicit def implicits[T <: U: ValueOf, U: Numeric] = {
            Applyable.implicits[gt[T, U]]( new gt[T, U] )
        }
    }

    class gte[T <: U: ValueOf, U: Numeric]
            extends Condition.With[U]( _ >= valueOf[T] )
            with Arguments.With[T] {
        override val argument = valueOf[T]
    }

    object gte {
        def apply[T: Numeric]( value: T ): gte[value.type, T] = new gte[value.type, T]

        implicit def implicits[T <: U: ValueOf, U: Numeric] = {
            Applyable.implicits[gte[T, U]]( new gte[T, U] )
        }
    }

    class isZero[T: Numeric]
        extends Condition.With[T]( _ == zero )
        with Arguments.Input

    object isZero {
        def apply[T: Numeric]: isZero[T] = new isZero[T]

        implicit def implicits[T: Numeric] = {
            Applyable.implicits[isZero[T]]( new isZero[T] )
        }
    }

    class negative[T: Numeric]
        extends Condition.With[T]( _ < zero )
        with Arguments.Input

    object negative {
        def apply[T: Numeric]: negative[T] = new negative[T]

        implicit def implicits[T: Numeric] = {
            Applyable.implicits[negative[T]]( negative[T] )
        }
    }

    class positive[T: Numeric]
        extends Condition.With[T]( _ > zero )
        with Arguments.Input

    object positive {
        def apply[T: Numeric]: positive[T] = new positive[T]

        implicit def implicits[T: Numeric] = {
            Applyable.implicits[positive[T]]( positive[T] )
        }
    }
}

object numeric extends numeric