package net.spals.leagur.store.hyperdex.impl

import java.util.{Optional, Collections}
import javax.annotation.PostConstruct

import net.spals.leagur.store.key.StoreKey
import org.hyperdex.client.Client
import com.netflix.governator.annotations.{Configuration, AutoBindSingleton}
import net.spals.leagur.model.Entity
import net.spals.leagur.store.Store

import scala.collection.JavaConverters._

import javax.validation.constraints.NotNull

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
  private lazy val hyperdexEndpoint: String = null

  private var hyperdex: Client = null;

  @PostConstruct
  def postConstruct(): Unit = {
    hyperdex = new Client(hyperdexEndpoint, hyperdexPort)
  }

  @Override
  def migrate(): Unit = {

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
