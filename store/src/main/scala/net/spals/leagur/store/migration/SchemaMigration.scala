package net.spals.leagur.store.migration

/**
 * @author tkral
 */
trait SchemaMigration[C] {
  def migrate(client: C): Unit
}
