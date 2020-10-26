package cc.ayakurayuki.csa.starter.api

import cc.ayakurayuki.csa.starter.core.annotation.Token
import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.JsonResponse
import cc.ayakurayuki.csa.starter.service.ContentService
import io.vertx.core.Future

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * @author ayakurayuki* @date 2019/07/27-11:31
 */
@Path(value = '/content')
class ContentRest extends BaseRest {

  private ContentService contentService

  ContentRest() {
    contentService = Constants.injector.getInstance ContentService.class
  }

  @POST
  @Path(value = '/search')
  @Produces(MediaType.APPLICATION_JSON)
  @Token
  Future<JsonResponse> search(@FormParam('keyword') @DefaultValue("") String keyword,
                              @FormParam('token') String token) {
    def result = [] as LinkedHashMap<String, Object>
    def data = contentService.search(keyword)
        .compose { ar ->
          result['list'] = ar
          Future.<LinkedHashMap<String, Object>> succeededFuture result
        }
    response data
  }

}
