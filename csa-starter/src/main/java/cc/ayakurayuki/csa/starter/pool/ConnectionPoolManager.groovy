package cc.ayakurayuki.csa.starter.pool

import io.vertx.core.Handler
import io.vertx.ext.sql.SQLClient
import io.vertx.ext.sql.SQLConnection

/**
 *
 * @author ayakurayuki* @date 2019/05/20-15:34
 */
interface ConnectionPoolManager {

  void getConnection(Handler<SQLConnection> callback)

  SQLClient getSQLClient()

}
