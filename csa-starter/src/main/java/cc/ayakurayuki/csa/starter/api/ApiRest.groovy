package cc.ayakurayuki.csa.starter.api

import cc.ayakurayuki.csa.starter.core.entity.JsonResponse

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * @author ayakurayuki* @date 2019/05/20-16:46
 */
@Path(value = '/api')
class ApiRest extends BaseRest {

  private ApiRest() {

  }

  @GET
  @Path(value = 'ping')
  @Produces(MediaType.APPLICATION_JSON)
  static JsonResponse ping() {
    new JsonResponse(0, 'pong', [
        't': System.currentTimeMillis()
    ])
  }

}
