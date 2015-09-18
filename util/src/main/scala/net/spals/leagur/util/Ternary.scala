package net.spals.leagur.util

/**
 * Hand rolled ternary operator.
 *
 * See:
 * http://blog.tmorris.net/posts/does-scala-have-javas-ternary-operator/
 *
 * @author tkral
 */
object Ternary {
  implicit def TernaryBool(b: Boolean) = Ternary(b)
}

case class Ternary(b: Boolean) {
  def ?[X](t: => X) = new {
    def |(f: => X) = if(b) t else f
  }
}