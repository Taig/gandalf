package io.taig.gandalf.core

import cats.data.Validated._
import shapeless._

trait Transformation extends Mutation with Arguments.None {
    override final def mutate( input: Input ): Option[Output] = {
        Some( transform( input ) )
    }

    def transform( input: Input ): Output
}

object Transformation {
    type Input[I] = Transformation { type Input = I }

    type Output[O] = Transformation { type Output = O }

    type Aux[I, O] = Transformation { type Input = I; type Output = O }

    abstract class With[I, O]( f: I ⇒ O ) extends Transformation {
        override final type Input = I

        override final type Output = O

        override final def transform( input: Input ) = f( input )
    }

    implicit def validation[T <: Transformation](
        implicit
        w: Witness.Aux[T]
    ): Validation[T] = Validation.instance[T] { input ⇒
        valid( w.value.transform( input.asInstanceOf[w.value.Input] ) )
    }
}