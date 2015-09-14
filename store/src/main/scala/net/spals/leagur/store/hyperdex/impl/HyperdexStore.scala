package net.spals.leagur.store.hyperdex.impl

import java.util.{Optional, Collections}

import com.netflix.governator.annotations.AutoBindSingleton
import net.spals.leagur.model.Entity
import net.spals.leagur.store.Store

import scala.collection.JavaConverters._

/**
 * Hyperdex implementation of {@link Store}
 *
 * @author tkral
 */
@AutoBindSingleton(classOf[Store])
class HyperdexStore extends Store {

  @Override
  def all(tableName: String, key: Any): List[Entity] = {
    Collections.emptyList().asScala.toList
  }

  @Override
  def delete(tableName: String, key: Any): Unit = {  }

  @Override
  def get(tableName: String, key: Any): Option[Entity] = {
    Some(new Entity)
  }

  @Override
  def post(tableName: String, key: Any, entity: Entity): Entity = {
    new Entity
  }

  @Override
  def put(tableName: String, key: Any, entity: Entity): Entity = {
    new Entity
  }
}
