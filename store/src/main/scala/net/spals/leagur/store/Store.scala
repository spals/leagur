package net.spals.leagur.store

import net.spals.leagur.model.Entity

/**
 * An interface contract to interact with
 * a backend store.
 *
 * @author tkral
 */
trait Store {

  def all(tableName: String, key: String): List[Entity]

  def delete(tableName: String, key: String): Unit

  def get(tableName: String, key: String): Entity

  def post(tableName: String, key: String, entity: Entity): Entity

  def put(tableName: String, key: String, entity: Entity): Entity
}
