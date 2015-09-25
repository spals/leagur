package net.spals.leagur.api

import java.util.UUID
import javax.ws.rs._
import javax.ws.rs.core.{Response, MediaType}

import com.google.inject.Inject
import com.netflix.governator.annotations.AutoBindSingleton
import net.spals.leagur.store.Store
import net.spals.leagur.store.key.IdStoreKey

/**
 * RESTful API resource for teams
 *
 * @author tkral
 */
@Path("leagur/teams")
@Produces(Array(MediaType.APPLICATION_JSON))
@Consumes(Array(MediaType.APPLICATION_JSON))
@AutoBindSingleton
case class TeamsResource @Inject() (store: Store) {

  @GET
  @Path("/{id}")
  def get(@PathParam("id") id: UUID): Response = {
    val teamEntity = store.get("teams", IdStoreKey(id))
    APIResponseBuilder.createReadResponse(teamEntity)
  }
}
