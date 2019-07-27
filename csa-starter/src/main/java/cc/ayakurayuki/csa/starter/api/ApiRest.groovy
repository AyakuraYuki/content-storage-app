package cc.ayakurayuki.csa.starter.api

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.JsonResponse
import cc.ayakurayuki.csa.starter.service.ContentService
import cc.ayakurayuki.csa.starter.service.SettingService
import io.vertx.core.Future

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * @author ayakurayuki* @date 2019/05/20-16:46
 */
@Path(value = '/api')
class ApiRest extends BaseRest {

  private SettingService settingService
  private ContentService contentService

  ApiRest() {
    settingService = Constants.injector.getInstance SettingService.class
    contentService = Constants.injector.getInstance ContentService.class
  }

  @GET
  @Path(value = '/ping')
  @Produces(MediaType.APPLICATION_JSON)
  Future<JsonResponse> ping() {
    def data = Future.future { f ->
      def result = [
          'message': 'pong'
      ] as LinkedHashMap<String, Object>
      f.complete result
    }
    response 0, 'pong', data
  }

  @GET
  @Path(value = '/token')
  @Produces(MediaType.APPLICATION_JSON)
  Future<JsonResponse> token() {
    def resultFuture = settingService.token.compose { ar ->
          def result = [
              'token': ar
          ]
          Future.<LinkedHashMap<String, Object>> succeededFuture result
        }
    response resultFuture
  }

//  @POST
//  @Path(value = '/secret')
//  @Produces(MediaType.APPLICATION_JSON)
//  @Token
//  Future<JsonResponse> secret(@FormParam('token') @DefaultValue('') String token) {
//    def resultFuture = settingService.secretKey
//        .compose({ ar ->
//          def result = [
//              'secret': ar
//          ]
//          Future.<LinkedHashMap<String, Object>> succeededFuture result
//        })
//    response resultFuture
//  }

//  @GET
//  @Path(value = '/des')
//  @Produces(MediaType.APPLICATION_JSON)
//  Future<JsonResponse> des() {
//    def result = [] as LinkedHashMap<String, Object>
//    def data = DesUtils.encryptFuture('233333')
//        .compose { ar ->
//          result['encrypted'] = ar
//          result['encryptedByOld'] = DesUtils.encrypt('233333')
//          DesUtils.decryptFuture(ar)
//        }
//        .compose { ar ->
//          result['decrypted'] = ar
//          result['decryptedByOld'] = DesUtils.decrypt(DesUtils.encrypt('233333'))
//          Future.<LinkedHashMap<String, Object>> succeededFuture result
//        }
//    response data
//  }

}
