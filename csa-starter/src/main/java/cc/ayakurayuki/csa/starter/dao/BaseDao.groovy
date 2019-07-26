package cc.ayakurayuki.csa.starter.dao

import cc.ayakurayuki.csa.starter.pool.ConnectionPoolManager
import cc.ayakurayuki.csa.starter.pool.HikariCPManager
import io.vertx.core.Handler
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/05/20-18:04
 */
class BaseDao {

  private static final def logger = LoggerFactory.getLogger BaseDao.class
  private static ConnectionPoolManager hikariCPM = HikariCPManager.getInstance()

  /**
   * 无参数查询
   *
   * @param sql 查询语句
   * @param callback 查询结束后的回调方法
   */
  static void query(String sql, Handler<List<JsonObject>> callback) {
    query sql, null, callback
  }

  /**
   * 带参数查询
   *
   * @param sql 查询语句
   * @param params 查询参数
   * @param callback 查询结束后的回调方法
   */
  static void query(String sql, JsonArray params, Handler<List<JsonObject>> callback) {
    hikariCPM.getConnection { conn ->
      conn.queryWithParams sql, params, { ar ->
        if (ar.succeeded()) {
          def rows = ar.result().rows
          callback.handle(rows)
        } else {
          throw new RuntimeException(ar.cause())
        }
        conn.close()
      }
    }
  }

  /**
   * 插入、更新、删除，带参数
   *
   * @param sql 插入/删除/更新语句
   * @param params SQL参数
   * @param callback 执行结束的回调方法，内容是影响的数据库行数
   */
  static void update(String sql, JsonArray params, Handler<Integer> callback) {
    executeGeneral sql, params, false, callback
  }

  /**
   * 插入一条数据，并获取自增的ID/字段
   *
   * @param sql 插入语句
   * @param params SQL参数
   * @param callback 执行结束的回调方法，内容是自增的新ID
   */
  static void insertGetNewId(String sql, JsonArray params, Handler<Integer> callback) {
    executeGeneral sql, params, true, callback
  }

  /**
   * 通用的带参数插入、更新、删除方法
   *
   * @param sql 插入语句
   * @param params SQL参数
   * @param getNewId true:将返回插入数据后的新ID，false:将返回执行操作影响的行数
   * @param callback 执行结束的回调方法，内容是影响的数据库行数
   */
  private static void executeGeneral(String sql, JsonArray params, boolean getNewId,
                                     Handler<Integer> callback) {
    hikariCPM.getConnection { conn ->
      conn.updateWithParams sql, params, { ar ->
        if (ar.succeeded()) {
          def result = ar.result()
          if (getNewId) {
            def newId = result.keys.getInteger(0)
            def rows = result.updated
            logger.debug "$rows effected, new ID is $newId"
            callback.handle(newId)
          } else {
            def rows = result.updated
            logger.debug "$rows effected..."
            callback.handle(rows)
          }
        } else {
          logger.error "Access database failed, ${ar.cause().localizedMessage}"
          callback.handle(-1)
        }
        conn.close()
      }
    }
  }

}
