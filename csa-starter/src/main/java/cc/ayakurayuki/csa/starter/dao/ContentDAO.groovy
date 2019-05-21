package cc.ayakurayuki.csa.starter.dao

import cc.ayakurayuki.csa.starter.core.entity.Content
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

/**
 * Content DAO
 * @author ayakurayuki* @date 2019/05/21-09:53
 */
class ContentDAO extends BaseDAO {

  private static final def logger = LoggerFactory.getLogger SettingDAO.class

  void get(String id, Handler<AsyncResult<JsonObject>> resultHandler) {
    def future = Future.<JsonObject> future()
    query 'SELECT * FROM content WHERE id = ?', new JsonArray().add(id), { ar ->
      future.complete ar.size() > 0 ? ar.get(0) : null
    }
    future.setHandler resultHandler
  }

  void list(Handler<AsyncResult<List<JsonObject>>> resultHandler) {
    def future = Future.<List<JsonObject>> future()
    query 'SELECT * FROM content ORDER BY item ASC', { ar ->
      future.complete ar.size() > 0 ? ar : new ArrayList<JsonObject>()
    }
    future.setHandler resultHandler
  }

  void search(String item, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
    def future = Future.<List<JsonObject>> future()
    query "SELECT * FROM content ${StringUtils.isNotEmpty(item) ? 'WHERE item LIKE \'%\' || ? || \'%\'' : ''} ORDER BY item ASC", new JsonArray().add(item), { ar ->
      future.complete ar.size() > 0 ? ar : new ArrayList<JsonObject>()
    }
    future.setHandler resultHandler
  }

  void save(Content content, Handler<AsyncResult<Integer>> resultHandler) {
    def future = Future.<Integer> future()
    update 'INSERT OR REPLACE INTO content (id, item, jsonData) VALUES (?, ?, ?)', new JsonArray().add(content.id).add(content.item).add(content.jsonData), { ar ->
      future.complete ar
    }
    future.setHandler resultHandler
  }

  void delete(String id, Handler<AsyncResult<Integer>> resultHandler) {
    def future = Future.<Integer> future()
    update 'DELETE FROM content WHERE id = ?', new JsonArray().add(id), { ar ->
      future.complete ar
    }
    future.setHandler resultHandler
  }

}
