package net.spals.leagur.store.key

import java.util.UUID

import net.spals.leagur.model.Entity

/**
 * An abstraction of a key against which
 * a {@link Store} record is stored.
 *
 * @author tkral
 */
trait StoreKey {
  /**
   * The name of the key field.
   */
  def fieldName: String

  /**
   * Return the key value which is used to
   * retrieve a {@link Store} record.
   */
  def value: String
}

case class IdStoreKey(idValue: UUID) extends StoreKey {
  override def fieldName = Entity.ID_KEY
  override def value = idValue.toString
}

case class GenericStoreKey(fieldName: String, rawValue: AnyRef) extends StoreKey {
  override def value = rawValue.toString
}
