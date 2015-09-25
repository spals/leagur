package net.spals.leagur.store.migration

import java.lang.reflect.Method

/**
 * @author tkral
 */
class MigrationProvider[M <: SchemaMigration[_]](migrationType: Class[M],
                                                 migrationClass: Class[_],
                                                 migrationClasses: Class[_]*) {

  def getPending(currentVersion: Long): Map[Migration, M] = {
    Map()
//    val allMigrationMethods: Map[Migration, Method] = scanForMigrations(migrationClass, migrationClasses)
//    val allMigrations: Map[Migration, SchemaMigration[_]] = mapAllMigrations(migrationType, allMigrationMethods)
//    val pendingMigrations: Map[Migration, M] = new TreeMap[Migration, M](migrationComparator)
//    allMigrations.entrySet.forEach(migrationEntry -> {
//      if (migrationEntry.getKey().version() > currentVersion) {
//        LOGGER.info("Adding migration({}): {}", migrationEntry.getKey().version(), migrationEntry.getKey().description());
//        pendingMigrations.put(migrationEntry.getKey(), (M) migrationEntry.getValue());
//      } else {
//        LOGGER.info("Already ran migration({}): {}",
//          migrationEntry.getKey().version(), migrationEntry.getKey().description());
//      }
//    })
//    pendingMigrations
  }

//  private def scanForMigrations(): Map[Migration, Method] = {
//
//  }
}
