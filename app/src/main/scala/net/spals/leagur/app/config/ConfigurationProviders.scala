package net.spals.leagur.app.config

import io.dropwizard.{Configuration, Application}
import io.dropwizard.configuration.ConfigurationSourceProvider
import java.io.IOException
import java.io.InputStream

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