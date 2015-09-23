package net.spals.leagur.app

import java.util.concurrent.atomic.AtomicReference

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.google.common.base.Preconditions.checkNotNull
import com.google.common.collect.Lists
import com.google.inject.Injector
import com.netflix.governator.guice.{BootstrapBinder, BootstrapModule, LifecycleInjector}
import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigResolveOptions}
import io.dropwizard.configuration.{EnvironmentVariableSubstitutor, SubstitutingSourceProvider}
import io.dropwizard.setup.{Bootstrap, Environment}
import io.dropwizard.{Application, Configuration}
import net.spals.leagur.api.{AboutResource, TeamsResource}
import net.spals.leagur.app.config.{ClassLoaderConfigurationSourceProvider, TypesafeConfigurationProvider}
import net.spals.leagur.store.Store
import net.spals.leagur.store.migration.MigrationRunner
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

/**
 * @author tkral
 */
object LeagurApp {

  val LEAGUR_APP_CONFIG_FILE_NAME = "config/leagur-app.yml"
  val LEAGUR_SERVICE_CONFIG_FILE_NAME = "config/leagur-service.conf"
  val LOGGER: Logger = LoggerFactory.getLogger(classOf[LeagurApp])

  def main (args: Array[String]) {
    val appArgs = defaultAppArgs(args)
    try {
      new LeagurApp(LEAGUR_SERVICE_CONFIG_FILE_NAME).run(appArgs:_*)
    } catch {
      case t: Throwable => {
        LOGGER.error(s"Error running command line with arguments: $appArgs", t)
      }
    }
  }

  def defaultAppArgs(args: Array[String]): Array[String] = {
    checkNotNull(args)
    val argList = Lists.newArrayList(args:_*)
    // If no command line arguments are provided then default to
    // the server argument
    if (argList.isEmpty) {
      argList.add("server")
    }

    // Add in the application configuration
    argList.add(LEAGUR_APP_CONFIG_FILE_NAME)
    argList.asScala.toList.toArray
  }
}

class LeagurApp(serviceConfigFileName: String) extends Application[Configuration] {

  private val cachedInjector: AtomicReference[Injector] = new AtomicReference[Injector]

  override def getName = "leagur"

  override def initialize(bootstrap: Bootstrap[Configuration]): Unit = {
    LeagurApp.LOGGER.info("Initializing Leagur application...")
    // Register the Jackson scala module
    bootstrap.getObjectMapper.registerModule(new DefaultScalaModule)
    // Setup to provide application configuration from the classpath
    bootstrap.setConfigurationSourceProvider(
      new SubstitutingSourceProvider(
        new ClassLoaderConfigurationSourceProvider(this.getClass.getClassLoader),
        new EnvironmentVariableSubstitutor(false)))
  }

  override def run(appConfig: Configuration, env: Environment): Unit = {
    try {
      val serviceConfig = ConfigFactory.load(serviceConfigFileName,
        ConfigParseOptions.defaults.setAllowMissing(false),
        ConfigResolveOptions.defaults)

      // Register API resources with Jersey
      val injector = createInjector(serviceConfig)
      env.jersey().register(injector.getInstance(classOf[AboutResource]))
      env.jersey().register(injector.getInstance(classOf[TeamsResource]))

      // Run the store upgrade scripts
      val migrationRunner = injector.getInstance(classOf[MigrationRunner])
      migrationRunner.runMigration
    } catch {
      case t: Throwable => {
        LeagurApp.LOGGER.error("Error running Leagur !!", t)
        throw t
      }
    }
  }

  @throws(classOf[Exception])
  def createInjector(serviceConfig: Config): Injector = {
    val lifecycleInjector = LifecycleInjector.builder()
      // Use governator to scan the Leagur packages
      // on the classpath
      .usingBasePackages("net.spals.leagur")
      .withBootstrapModule(
        new BootstrapModule {
          override def configure(binder: BootstrapBinder): Unit = {
            binder.bindConfigurationProvider().toInstance(new TypesafeConfigurationProvider(serviceConfig))
          }
        }
      )
      .build

    val manager = lifecycleInjector.getLifecycleManager
    manager.start
    sys.ShutdownHookThread {
      // Ensure the EventConsumer is shutdown when we exit
      LeagurApp.LOGGER.info("Shutting down Leagur application...")
      manager.close
    }

    lifecycleInjector.createInjector()
  }
}
