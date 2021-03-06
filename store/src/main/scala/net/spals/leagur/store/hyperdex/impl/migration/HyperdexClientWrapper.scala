package net.spals.leagur.store.hyperdex.impl.migration

import javax.annotation.PostConstruct
import javax.script.{ScriptEngine, ScriptEngineManager}
import javax.validation.constraints.NotNull

import com.google.common.base.Joiner
import com.netflix.governator.annotations.{Configuration, AutoBindSingleton}
import org.hyperdex.client.Client

import scala.collection.JavaConverters._

/**
 * @author tkral
 */
@AutoBindSingleton
class HyperdexClientWrapper {

  @NotNull
  @Configuration(value = "hyperdex.store.port")
  private var hyperdexPort: Int = 0

  @NotNull
  @Configuration(value = "hyperdex.store.host")
  private var hyperdexHost: String = null

  var nativeClient: Client = null
  private val scriptEngine: ScriptEngine = new ScriptEngineManager().getEngineByName("jython")

  @PostConstruct
  def postConstruct = {
    nativeClient = new Client(hyperdexHost, hyperdexPort)
    scriptEngine.eval("import hyperdex.admin")
    scriptEngine.eval(s"adminClient = hyperdex.admin.Admin('$hyperdexHost', $hyperdexPort)")
  }

  def addSpace(
    spaceName: String,
    keyName: String,
    attributes: List[String],
    subspace: List[String],
    numPartitions: Int = 8,
    numTolerance: Int = 2
  ) = {
    val attributesStr = Joiner.on(',').join(attributes.asJava)
    val subspaceStr = Joiner.on(',').join(subspace.asJava)

    scriptEngine.eval(s"adminClient.add_space(" +
      s"space $spaceName " +
      s"key $keyName " +
      s"attributes $attributesStr " +
      s"subspace $subspaceStr " +
      s"create $numPartitions partitions " +
      s"tolerate $numTolerance failures)"
    )
  }


}
