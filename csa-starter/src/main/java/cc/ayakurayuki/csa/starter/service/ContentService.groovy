package cc.ayakurayuki.csa.starter.service

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.Content
import cc.ayakurayuki.csa.starter.core.util.IdUtils
import cc.ayakurayuki.csa.starter.dao.ContentDao
import cc.ayakurayuki.csa.starter.util.DesUtils
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.apache.commons.lang3.StringUtils
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
        final def content = Json.decodeValue object.encode(), Content.class
        // Remove jsonData because this field is never used in list view.
        content.jsonData = ''
        return content
      }
      Future.succeededFuture list
    }
  }

  Future<Content> get(String id) {
    Future.<JsonObject> future { f ->
      contentDao.get id, f
    }.compose { ar ->
      Future.succeededFuture Json.decodeValue(ar.encode(), Content.class)
    }.compose { content ->
      DesUtils.decryptFuture(content.jsonData).compose { json ->
        content.jsonData = json
        Future.succeededFuture content
      }
    }
  }

  Future<Integer> save(String id, String item, String jsonData) {
    get(id).compose { content ->
      if (null == content) {
        content = new Content()
        content.id = IdUtils.UUID()
      }
      if (StringUtils.isNotEmpty(item)) {
        content.item = item
      }
      return Future.<Content> succeededFuture(content)
    }.compose { content ->
      if (StringUtils.isNotEmpty(jsonData)) {
        return DesUtils.encryptFuture(jsonData)
            .compose { data ->
              content.jsonData = data
              return Future.<Content> succeededFuture(content)
            }
      } else {
        return Future.<Content> succeededFuture(content)
      }
    }.compose { content ->
      return Future.<Integer> future { f -> contentDao.save content, f }
    }
  }

  Future<Integer> delete(String id) {
    get(id).compose { content ->
      if (null == content) {
        return Future.succeededFuture(0)
      } else {
        return Future.<Integer> future { f -> contentDao.delete id, f }
      }
    }
  }

}
