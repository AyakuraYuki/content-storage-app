package cc.ayakurayuki.csa.starter.api

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.JsonResponse
import cc.ayakurayuki.csa.starter.service.SettingService
import cc.ayakurayuki.csa.starter.util.DESUtils
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

  ApiRest() {
    settingService = Constants.injector.getInstance SettingService.class
  }

  @GET
  @Path(value = 'ping')
  @Produces(MediaType.APPLICATION_JSON)
  Future<JsonResponse> ping() {
    def data = Future.future { f ->
      def result = [
          't': System.currentTimeMillis()
      ]
      f.complete result
    }
    response 0, 'pong', data
  }

  @GET
  @Path(value = 'token')
  @Produces(MediaType.APPLICATION_JSON)
  Future<JsonResponse> token() {
    def resultFuture = settingService[Constants.TOKEN]
        .compose({ ar ->
          Future.future({ f ->
            def result = [
                'setting': ar
            ]
            f.complete result
          })
        })
    response resultFuture
  }

  @GET
  @Path(value = 'secret')
  @Produces(MediaType.APPLICATION_JSON)
  Future<JsonResponse> secret() {
    def resultFuture = settingService.secretKey
        .compose({ ar ->
          Future.future({ f ->
            def result = [
                'secret': ar
            ]
            f.complete result
          })
        })
    response resultFuture
  }

  @GET
  @Path(value = 'des')
  @Produces(MediaType.APPLICATION_JSON)
  Future<JsonResponse> des() {
    def data = DESUtils.encrypt '23333'
    def decrypted = DESUtils.decrypt data
    def resultFuture = DESUtils.decryptFuture(data)
        .compose { ar ->
          Future.future { f ->
            def result = [
                'old': decrypted,
                'new': ar
            ]
            f.complete result
          }
        }
    response resultFuture
  }

}
