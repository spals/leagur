package net.spals.leagur.app.config

import java.io.{IOException, InputStream}
import java.util.{Date, Map}

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.governator.configuration.{ConfigurationKey, DefaultConfigurationProvider, Property}
import com.typesafe.config.{Config, ConfigException}
import io.dropwizard.configuration.ConfigurationSourceProvider
import net.spals.leagur.util.Ternary._
import org.slf4j.{Logger, LoggerFactory}

/**
 * A {@link ConfigurationSourceProvider} which opens a configuration
 * file using a {@link ClassLoader}
 *
 * @author tkral
 */
class ClassLoaderConfigurationSourceProvider(cl: ClassLoader) extends ConfigurationSourceProvider {

  @throws(classOf[IOException])
  def open(path: String): InputStream = {
    return cl.getResourceAsStream(path)
  }
}

class TypesafeConfigurationProvider(config: Config) extends DefaultConfigurationProvider {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[TypesafeConfigurationProvider])

  private val mapper = new ObjectMapper

  override def has(key: ConfigurationKey) = config.hasPath(key.getRawKey)

  override def getBooleanProperty(configKey: ConfigurationKey, defaultValue: java.lang.Boolean): Property[java.lang.Boolean] = {
    new Property[java.lang.Boolean] {
      override def get: java.lang.Boolean = {
        try {
          config.getBoolean(configKey.getRawKey)
        } catch {
          case e: ConfigException.Missing => defaultValue
        }
      }
    }
  }

  override def getIntegerProperty(configKey: ConfigurationKey, defaultValue: java.lang.Integer): Property[java.lang.Integer] = {
    new Property[java.lang.Integer] {
      override def get: java.lang.Integer = {
        try {
          config.getInt(configKey.getRawKey)
        } catch {
          case e: ConfigException.Missing => defaultValue
        }
      }
    }
  }

  override def getLongProperty(configKey: ConfigurationKey, defaultValue: java.lang.Long): Property[java.lang.Long] = {
    new Property[java.lang.Long] {
      override def get(): java.lang.Long = {
        try {
          config.getLong(configKey.getRawKey)
        } catch {
          case e: ConfigException.Missing => defaultValue
        }
      }
    }
  }

  override def getDoubleProperty(configKey: ConfigurationKey, defaultValue: java.lang.Double): Property[java.lang.Double] = {
    new Property[java.lang.Double] {
      override def get(): java.lang.Double = {
        try {
          config.getDouble(configKey.getRawKey)
        } catch {
          case e: ConfigException.Missing => defaultValue
        }
      }
    }
  }

  override def getStringProperty(configKey: ConfigurationKey, defaultValue: String): Property[String] = {
    new Property[String] {
      override def get(): String = {
        try {
          config.getString(configKey.getRawKey)
        } catch {
          case e: ConfigException.Missing => defaultValue
        }
      }
    }
  }

  override def getDateProperty(key: ConfigurationKey, defaultValue: Date): Property[Date] = {
    throw new UnsupportedOperationException("Date configuration is not supported")
  }

  override def getObjectProperty[T](key: ConfigurationKey, defaultValue: T, objectType: Class[T]): Property[T] = {
    new Property[T] {
      override def get(): T = {
        try {
          val serialized: String = getStringSupplier(key, null).get

          serialized match {
            case null | "" => defaultValue
            case s => classOf[Map[_,_]].isAssignableFrom(objectType) ?
              mapper.readValue(serialized, objectType) |
              mapper.readValue(s"$serialized", objectType)
          }
        } catch {
          case e: ConfigException.Missing => defaultValue
        }
      }
    }
  }
}
