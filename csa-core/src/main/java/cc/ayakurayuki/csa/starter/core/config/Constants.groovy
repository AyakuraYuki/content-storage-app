package cc.ayakurayuki.csa.starter.core.config


import com.google.inject.Injector
import io.vertx.core.Context
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.redis.RedisClient
import io.vertx.redis.RedisOptions
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/05/20-15:15
 */
class Constants {

  private static final def logger = LoggerFactory.getLogger Constants.class

  private static Vertx vertx
  private static Context context
  private static EventBus eventBus
  private static RedisClient redis
  public static Injector injector

  public static final def EMPTY = ''
  public static final def SECRET = 'secret'
  public static final def EMERGENCY = 'emergency'
  public static final def DES_KEY = 'desKey'
  public static final def TOKEN = 'token'

  enum ResponseCode {
    NULL(-1000),
    ERROR(-1),
    OK(0)

    int code

    ResponseCode(int code) {
      this.code = code
    }
  }

  enum ErrorCode {
    AUTH_FAILED(-10000),
    ALL_EMERGENCY_CODE_USED(-10001),
    DES_DECRYPT_ERROR(-10002),
    DES_ENCRYPT_ERROR(-10003),
    OTHERS(-500)

    final int code

    ErrorCode(int code) {
      this.code = code
    }
  }

  static Vertx getVertx() {
    return vertx
  }

  static Context getContext() {
    return context
  }

  static EventBus getEventBus() {
    return eventBus
  }

  static RedisClient getRedis() {
    return redis
  }

  static <T> Future<T> executeBlocking(Handler<Future<T>> blockingCodeHandler) {
    Future<T> future = Future.future()
    Constants.vertx.executeBlocking(blockingCodeHandler, { asyncResult ->
      if (asyncResult.succeeded()) {
        future.complete(asyncResult.result())
      } else {
        future.fail(asyncResult.cause())
      }
    })
    return future
  }

  static def init(Context initContext) {
    vertx = initContext.owner()
    context = initContext
    eventBus = vertx.eventBus()

    def config = Constants.context.config()
    def redisOptions = new RedisOptions()
    redisOptions.host = config.getString 'redis.host', '127.0.0.1'
    redisOptions.port = config.getInteger 'redis.port', 6379
    def enableRedis = config.getBoolean 'redis.enable', false
    logger.info "Redis cache management: ${enableRedis ? 'enabled' : 'disabled'}"
    if (enableRedis) {
      redis = RedisClient.create Constants.vertx, redisOptions
      logger.info "Redis client is ready to go."
    } else {
      logger.info "Disabled redis client, skip the redis client initialization."
    }
  }

}
