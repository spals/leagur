package net.spals.leagur.store

import java.util.Optional

import net.spals.leagur.model.Entity

/**
 * An interface contract to interact with
 * a backend store.
 *
 * @author tkral
 */
trait Store {

  def all(tableName: String, key: Any): List[Entity]

  def delete(tableName: String, key: Any): Unit

  def get(tableName: String, key: Any): Option[Entity]

  def post(tableName: String, key: Any, entity: Entity): Entity

  def put(tableName: String, key: Any, entity: Entity): Entity
}
