package net.spals.leagur.api.rest;

import com.google.common.collect.ImmutableMap;
import net.spals.appbuilder.annotations.service.AutoBindSingleton;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * RESTful API resource for showing server information
 *
 * @author tkral
 */
@Path("leagur/about")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AutoBindSingleton
class AboutResource {

    @GET
    public Response get() {
        return Response.ok(ImmutableMap.of("version", "1.0.0")).build();
    }
}
