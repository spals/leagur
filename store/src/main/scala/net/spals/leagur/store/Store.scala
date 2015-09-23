package net.spals.leagur.store

import net.spals.leagur.model.Entity
import net.spals.leagur.store.key.StoreKey

/**
 * An interface contract to interact with
 * a backend store.
 *
 * @author tkral
 */
trait Store {

  def all(tableName: String, key: StoreKey): List[Entity]

  def delete(tableName: String, key: StoreKey): Unit

  def get(tableName: String, key: StoreKey): Option[Entity]

  def post(tableName: String, key: StoreKey, entity: Entity): Entity

  def put(tableName: String, key: StoreKey, entity: Entity): Entity
}
