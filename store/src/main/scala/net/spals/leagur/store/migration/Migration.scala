package net.spals.leagur.store.migration

import scala.annotation.StaticAnnotation

/**
 * @author tkral
 */
case class Migration(num: Int, description: String) extends StaticAnnotation

