package net.spals.leagur.store.hyperdex.impl.migration

import java.util.Collections

import com.google.inject.Inject
import com.netflix.governator.annotations.AutoBindSingleton
import net.spals.leagur.store.migration.{Migration, MigrationProvider, MigrationRunner}
import org.hyperdex.client.HyperDexClientException

import scala.collection.JavaConverters._

/**
 * @author tkral
 */
object HyperdexMigrationRunner {
  private val MIGRATIONS_TABLE = "migrations"
  private val NUM_KEY = "num"
  private val DESCRIPTION_KEY = "description"

}
@AutoBindSingleton(classOf[MigrationRunner])
class HyperdexMigrationRunner @Inject() (hyperdex: HyperdexClientWrapper) extends MigrationRunner {

  private val migrationProvider = new MigrationProvider[HyperdexSchemaMigration](classOf[HyperdexSchemaMigration],
    HyperdexMigrations.getClass)

  def currentVersion(): Long = {
    try {
      val numMigations = hyperdex.nativeClient.count(HyperdexMigrationRunner.MIGRATIONS_TABLE, Collections.emptyMap())
      numMigations - 1
    } catch {
      case e: HyperDexClientException =>
        e.symbol() match {
          case "HYPERDEX_CLIENT_UNKNOWNSPACE" =>
            hyperdex.addSpace(HyperdexMigrationRunner.MIGRATIONS_TABLE, HyperdexMigrationRunner.NUM_KEY,
              List(HyperdexMigrationRunner.DESCRIPTION_KEY),
              List())
            -1L
          case _ => throw e
        }
      case t: Throwable => throw t
    }
  }

  override def runMigration(): Unit = {
    // Define a migration function. Given a Map entry of (Migration -> HyperdexSchemaMigration)
    val migrateFn: (((Migration, HyperdexSchemaMigration)) => Unit) = entry => {
      // 1. Add the migration record to the migrations table
      hyperdex.nativeClient.put_if_not_exist(HyperdexMigrationRunner.MIGRATIONS_TABLE, entry._1.num,
        Map[String, AnyRef]().asJava)
//        Map[String, AnyRef](HyperdexMigrationRunner.DESCRIPTION_KEY, entry._1.description).asJava)
      // 2. Run the migration
      entry._2.migrate(hyperdex)
    }

    val pendingMigrations = migrationProvider.getPending(currentVersion())
    pendingMigrations.foreach(migrateFn)
  }
}
