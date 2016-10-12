package io.taig.gandalf.macros

import io.taig.gandalf.core.{ Container, Rule, Validation }

import scala.language.experimental.macros
import scala.language.implicitConversions

trait implicits {
    implicit def valueToObey[I, C <: Container { type Kind <: Rule.Input[I] }]( input: I )(
        implicit
        v: Validation[C]
    ): I Obey C = macro lift.implementation[I, C]
}

object implicits extends implicits