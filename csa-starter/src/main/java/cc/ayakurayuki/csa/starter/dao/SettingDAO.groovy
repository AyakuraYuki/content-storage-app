package cc.ayakurayuki.csa.starter.dao

import cc.ayakurayuki.csa.starter.core.entity.Setting
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory

/**
 * Setting DAO
 * @author ayakurayuki* @date 2019/05/20-18:13
 */
class SettingDAO extends BaseDAO {

  private final def logger = LoggerFactory.getLogger SettingDAO.class

  void get(String id, Handler<AsyncResult<JsonObject>> resultHandler) {
    def future = Future.<JsonObject> future()
    def sql = 'SELECT * FROM settings WHERE id = ?'
    def param = new JsonArray().add(id)
    query sql, param, { ar -> future.complete ar.size() > 0 ? ar.get(0) : null }
    future.setHandler resultHandler
  }

  void getByKey(String key, Handler<AsyncResult<JsonObject>> resultHandler) {
    def future = Future.<JsonObject> future()
    def sql = 'SELECT * FROM settings WHERE key = ?'
    def param = new JsonArray().add(key)
    query sql, param, { ar -> future.complete ar.size() > 0 ? ar.get(0) : null }
    future.setHandler resultHandler
  }

  void save(Setting setting, Handler<AsyncResult<Integer>> resultHandler) {
    def future = Future.<Integer> future()
    def sql = 'INSERT OR REPLACE INTO settings (id, `key`, value) VALUES (?, ?, ?)'
    def param = new JsonArray()
        .add(setting.id)
        .add(setting.key)
        .add(setting.value)
    update sql, param, { ar -> future.complete ar }
    future.setHandler resultHandler
  }

  void delete(String key, Handler<AsyncResult<Integer>> resultHandler) {
    def future = Future.<Integer> future()
    def sql = 'DELETE FROM settings WHERE key = ?'
    def param = new JsonArray().add(key)
    update sql, param, { ar -> future.complete ar }
    future.setHandler resultHandler
  }

}
