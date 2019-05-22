package cc.ayakurayuki.csa.starter.pool

import cc.ayakurayuki.csa.starter.core.config.Constants
import io.vertx.core.Context
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider
import io.vertx.ext.sql.SQLClient
import io.vertx.ext.sql.SQLConnection
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/05/20-15:35
 */
class HikariCPManager implements ConnectionPoolManager {

  private final def logger = LoggerFactory.getLogger HikariCPManager.class
  private volatile static HikariCPManager INSTANCE = null
  private JDBCClient client

  HikariCPManager(Context context) {
    def config = context.config()
    def properties = [
        'provider_class' : config.getString('provider_class', HikariCPDataSourceProvider.class.name),
        'jdbcUrl'        : config.getValue('db.url'),
        'driverClassName': config.getValue('db.driver'),
        'username'       : config.getValue('db.user'),
        'password'       : config.getValue('db.pass'),
        'minimumIdle'    : config.getInteger('db.minimumIdle', 2),
        'maximumPoolSize': config.getInteger('db.maximumPoolSize', 10)
    ]
    def cpConfig = new JsonObject(properties)
    this.client = JDBCClient.createShared Constants.vertx, cpConfig, 'content-storage-app'
  }

  static HikariCPManager getInstance() {
    INSTANCE
  }

  static void init() {
    long start = System.currentTimeMillis()
    if (INSTANCE != null) {
      throw new RuntimeException('HikariCPManager is already initialized, please DON\'T invoke init()!!!')
    }
    def vertx = Constants.vertx
    if (vertx == null) {
      throw new RuntimeException('Constants must be initialized first!!!')
    }
    INSTANCE = new HikariCPManager(Constants.context)
    def end = System.currentTimeMillis()
    INSTANCE.logger.info "Done <${(end - start) / 1000}s>! HikariCP initialized."
  }

  static void close() {
    INSTANCE.client.close({ r ->
      logger.info "HikariCP ${r.succeeded() ? 'is closed.' : "cannot be closed, ${r.cause().localizedMessage}"}"
    })
  }

  @Override
  SQLClient getSQLClient() {
    this.client
  }

  @Override
  void getConnection(Handler<SQLConnection> callback) {
//    logger.info "JDBC client: ${client}"
    client.getConnection({ ar ->
      if (ar.succeeded()) {
        callback.handle ar.result()
      } else {
        logger.error "Cannot get SQL connection", ar.cause()
      }
    })
  }

}
