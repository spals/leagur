package net.spals.leagur.api

import javax.ws.rs.core.{MediaType, Response}
import javax.ws.rs.{GET, Consumes, Path, Produces}

import com.netflix.governator.annotations.AutoBindSingleton
import net.spals.leagur.model.Entity

/**
 * RESTful API resource for showing server information
 *
 * @author tkral
 */
@Path("leagur/about")
@Produces(Array(MediaType.APPLICATION_JSON))
@Consumes(Array(MediaType.APPLICATION_JSON))
@AutoBindSingleton
case class AboutResource() {

  @GET
  def get(): Response = {
    val aboutEntity = new Entity
    aboutEntity.put("Server version", "1.0.0")
    APIResponseBuilder.createReadResponse(aboutEntity)
  }
}
