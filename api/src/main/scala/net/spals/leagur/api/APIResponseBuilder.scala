package net.spals.leagur.api

import javax.ws.rs.NotFoundException
import javax.ws.rs.core.Response

/**
 * @author tkral
 */
object APIResponseBuilder {

  def createReadResponse(obj: Any): Response = {
    obj match {
      case null => throw new NotFoundException
      case None => throw new NotFoundException
      case Some(o) => Response.ok(o).build
      case _ => Response.ok(obj).build
    }
  }
}
