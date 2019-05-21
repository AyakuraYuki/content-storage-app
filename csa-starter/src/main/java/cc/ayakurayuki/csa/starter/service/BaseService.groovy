package cc.ayakurayuki.csa.starter.service

import cc.ayakurayuki.csa.starter.core.config.Constants
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

import java.text.MessageFormat

/**
 *
 * @author ayakurayuki* @date 2019/05/21-11:01
 */
class BaseService {

  protected def logger = LoggerFactory.getLogger getClass()
  def redis = Constants.redis
  public final def MAX_TIMESTAMP = 32503651200000L

  String format(String text, String... params) {
    MessageFormat.format text, params
  }

  def <T> Future<T> getByCacheElseDB(String key, Class<T> clazz,
                                     Handler<Future<JsonObject>> dbHandler,
                                     int cacheExpireTime) {
    return Future.<String> future { f -> redis?.get(key, f) }
        .compose { value ->
          if (StringUtils.isNotEmpty(value)) {
            return Future.future { f -> f.complete(Json.decodeValue(value, clazz)) }
          } else {
            return Future.future(dbHandler)
                .compose { r2 ->
                  def jsonStr = r2.encodePrettily()
                  if (cacheExpireTime > 0) {
                    redis?.setex key, cacheExpireTime, jsonStr, Future.future()
                  } else {
                    redis?.set key, jsonStr, Future.future()
                  }
                  return Future.succeededFuture(Json.decodeValue(jsonStr, clazz))
                }
          }
        }
  }

  def <T> Future<T> executeBlocking(Handler<Future<T>> blockingCodeHandler) {
    def future = Future.<T> future()
    Constants.vertx.executeBlocking blockingCodeHandler, { r ->
      if (r.succeeded()) {
        future.complete r.result()
      } else {
        future.fail r.cause()
      }
    }
    return future
  }

}
