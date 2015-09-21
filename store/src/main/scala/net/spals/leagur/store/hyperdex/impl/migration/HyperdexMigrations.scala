package net.spals.leagur.store.hyperdex.impl.migration

import net.spals.leagur.store.migration.{Migration, SchemaMigration}

/**
 * @author tkral
 */
trait HyperdexSchemaMigration extends SchemaMigration[HyperdexAdminWrapper]

object HyperdexMigrations {

  @Migration(0, "Create the teams table")
  def migration0() = {
    new HyperdexSchemaMigration {
      override def migrate(client: HyperdexAdminWrapper) = {
        val teamAttrs = List("name", "document info")
        val teamSubspaces = List("name")
        client.addSpace("teams", "id", teamAttrs, teamSubspaces)
      }
    }
  }
}
