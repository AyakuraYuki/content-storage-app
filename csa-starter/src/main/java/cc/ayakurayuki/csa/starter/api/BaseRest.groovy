package cc.ayakurayuki.csa.starter.api

import cc.ayakurayuki.csa.starter.core.entity.JsonResponse
import cc.ayakurayuki.csa.starter.provider.TokenProvider
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory

import java.util.function.Function

/**
 *
 * @author ayakurayuki* @date 2019/05/20-16:50
 */
class BaseRest {

  protected def logger = LoggerFactory.getLogger getClass()

  Future<Boolean> auth(String token, Handler<AsyncResult<Boolean>> callback) {
    def future = Future.<Boolean> future()
    def provider = new TokenProvider()
    def authInfo = new JsonObject().put('token', token)
    provider.authenticate authInfo, { result ->
      if (result.succeeded()) {
        future.complete true
      } else {
        future.fail result.cause()
      }
    }
    future.setHandler callback
  }

  def <T> Future<JsonResponse> response(Future<T> data) {
    return data.map({ obj ->
      new JsonResponse(data: obj)
    } as Function<T, JsonResponse>)
  }

  def <T> Future<JsonResponse> response(int status, String message, Future<T> data) {
    return data.map({ obj ->
      new JsonResponse(status: status, message: message, data: obj)
    } as Function<T, JsonResponse>)
  }

}
