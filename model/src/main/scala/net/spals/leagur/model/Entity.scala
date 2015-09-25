package net.spals.leagur.model

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
 * @author tkral
 */
object Entity {
  val ID_KEY: String = "id"
}

class Entity() extends mutable.LinkedHashMap[String, AnyRef] {

  def this(javaMap: java.util.Map[String, Object]) {
    this()
    this ++= javaMap.asScala
  }
}
