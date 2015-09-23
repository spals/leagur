package net.spals.leagur.store.hyperdex.impl

import java.util.Collections
import javax.annotation.PostConstruct
import javax.validation.constraints.NotNull

import com.netflix.governator.annotations.{AutoBindSingleton, Configuration}
import net.spals.leagur.model.Entity
import net.spals.leagur.store.Store
import net.spals.leagur.store.key.StoreKey
import org.hyperdex.client.Client

import scala.collection.JavaConverters._

/**
 * Hyperdex implementation of {@link Store}
 *
 * @author tkral
 */
@AutoBindSingleton(classOf[Store])
class HyperdexStore extends Store {

  @NotNull
  @Configuration(value = "hyperdex.store.port")
  private var hyperdexPort: Int = 0

  @NotNull
  @Configuration(value = "hyperdex.store.endpoint")
  private var hyperdexEndpoint: String = null

  private var hyperdex: Client = null;

  @PostConstruct
  def postConstruct(): Unit = {
    hyperdex = new Client(hyperdexEndpoint, hyperdexPort)
  }

  @Override
  def all(tableName: String, key: StoreKey): List[Entity] = {
    Collections.emptyList().asScala.toList
  }

  @Override
  def delete(tableName: String, key: StoreKey): Unit = {
    hyperdex.del(tableName, key.value)
  }

  @Override
  def get(tableName: String, key: StoreKey): Option[Entity] = {
    val result = hyperdex.get(tableName, key.value)
    result match {
      case m: java.util.Map[String, Object] => Some(new Entity(m))
      case _ => None
    }
  }

  @Override
  def post(tableName: String, key: StoreKey, entity: Entity): Entity = {
    hyperdex.put_if_not_exist(tableName, key.value, entity.asJava)
    entity
  }

  @Override
  def put(tableName: String, key: StoreKey, entity: Entity): Entity = {
    hyperdex.put(tableName, key.value, entity.asJava)
    entity
  }
}
