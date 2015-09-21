package net.spals.leagur.store.migration

import scala.annotation.StaticAnnotation

/**
 * @author tkral
 */
case class Migration(num: Int, description: String) extends StaticAnnotation

trait SchemaMigration[C] {
  def migrate(client: C): Unit
}
