package cc.ayakurayuki.csa.starter.api

import cc.ayakurayuki.csa.starter.core.entity.JsonResponse
import io.vertx.core.Future
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/05/20-16:50
 */
class BaseRest {

  protected def logger = LoggerFactory.getLogger getClass()

  def <T> Future<JsonResponse> response(Future<T> data) {
    data.compose { obj ->
      Future.<JsonResponse> succeededFuture new JsonResponse(obj)
    }
  }

  def <T> Future<JsonResponse> response(int status, String message, Future<T> data) {
    data.compose { obj ->
      Future.<JsonResponse> succeededFuture new JsonResponse(status, message, obj)
    }
  }

}
