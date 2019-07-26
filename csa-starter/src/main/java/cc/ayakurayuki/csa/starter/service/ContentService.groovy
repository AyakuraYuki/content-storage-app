package cc.ayakurayuki.csa.starter.service

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.Content
import cc.ayakurayuki.csa.starter.dao.ContentDao
import cc.ayakurayuki.csa.starter.util.DESUtils
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/07/26-16:14
 */
class ContentService extends BaseService {

  private def logger = LoggerFactory.getLogger getClass()
  private ContentDao contentDao

  ContentService() {
    contentDao = Constants.injector.getInstance ContentDao.class
  }

  Future<List<Content>> search(String keyword) {
    Future.<List<JsonObject>> future { f ->
      contentDao.search keyword, f
    }.compose { ar ->
      def list = ar.collect { object ->
        def content = Json.decodeValue object.encode(), Content.class
        content.jsonData = DESUtils.decrypt(content.jsonData)
        return content
      }
      Future.succeededFuture list
    }
  }

}
