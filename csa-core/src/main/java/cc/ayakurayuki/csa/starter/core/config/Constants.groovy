package cc.ayakurayuki.csa.starter.core.config

import com.google.inject.Injector
import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.redis.RedisClient
import io.vertx.redis.RedisOptions
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/05/20-15:15
 */
class Constants {

  private static final def logger = LoggerFactory.getLogger(Constants.class)

  private static Vertx vertx
  private static Context context
  private static RedisClient redis
  public static Injector injector

  static Vertx getVertx() {
    return vertx
  }

  static Context getContext() {
    return context
  }

  static RedisClient getRedis() {
    return redis
  }

  static def init(Context initContext) {
    vertx = initContext.owner()
    context = initContext

    def config = Constants.context.config()
    def redisOptions = new RedisOptions()
    redisOptions.host = config.getString 'redis.host', '127.0.0.1'
    redisOptions.port = config.getInteger 'redis.port', 6379
    def enableRedis = config.getBoolean 'redis.enable', false
    if (enableRedis) {
      redis = RedisClient.create Constants.vertx, redisOptions
      logger.info "Redis client is ready to go."
    } else {
      logger.info "Disabled redis client, skip the redis client initialization."
    }
  }

}
