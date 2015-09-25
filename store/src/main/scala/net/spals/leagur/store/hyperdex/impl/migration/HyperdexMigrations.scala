package net.spals.leagur.store.hyperdex.impl.migration

import net.spals.leagur.store.migration.{Migration, SchemaMigration}

/**
 * @author tkral
 */
trait HyperdexSchemaMigration extends SchemaMigration[HyperdexClientWrapper]

object HyperdexMigrations {

  @Migration(0, "Create the teams table")
  def migration0() = {
    new HyperdexSchemaMigration {
      override def migrate(client: HyperdexClientWrapper) = {
        client.addSpace("teams", "id",
          List("name", "document info") /*attributes*/,
          List("name") /*subspace*/)
      }
    }
  }
}
