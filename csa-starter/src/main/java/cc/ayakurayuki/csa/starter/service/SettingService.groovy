package cc.ayakurayuki.csa.starter.service

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.Setting
import cc.ayakurayuki.csa.starter.core.util.GoogleAuthenticator
import cc.ayakurayuki.csa.starter.core.util.IDUtils
import cc.ayakurayuki.csa.starter.dao.SettingDAO
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/05/21-11:01
 */
class SettingService extends BaseService {

  private def logger = LoggerFactory.getLogger getClass()
  private SettingDAO settingDAO

  SettingService() {
    settingDAO = Constants.injector.getInstance SettingDAO.class
  }

  Future<Setting> get(String id) {
    Future.<JsonObject> future { f ->
      settingDAO.get id, f
    }.compose { ar ->
      if (ar == null) {
        return Future.<Setting> succeededFuture(null)
      }
      def json = ar.encodePrettily()
      return Future.succeededFuture(Json.decodeValue(json, Setting.class))
    }
  }

  Future<Setting> getAt(String key) {
    Future.<JsonObject> future { f ->
      settingDAO.getByKey key, f
    }.compose { ar ->
      if (ar == null) {
        return Future.<Setting> succeededFuture(null)
      }
      def json = ar.encodePrettily()
      return Future.succeededFuture(Json.decodeValue(json, Setting.class))
    }
  }

  Future<Integer> save(Setting setting) {
    Future.<Integer> future { f ->
      settingDAO.save setting, f
    } .compose { ar ->
      return Future.succeededFuture(ar)
    }
  }

  Future<Object> delete(String key) {
    Future.<Integer> future { f ->
      settingDAO.delete key, f
    }.compose { ar ->
      if (ar > 0) {
        return Future.succeededFuture(ar)
      } else {
        return Future.failedFuture('No one has been deleted.')
      }
    }
  }

  Future<String> getSecretKey() {
    getAt(Constants.SECRET).compose { ar ->
      if (null == ar) {
        def setting = new Setting()
        setting.id = IDUtils.UUID()
        setting.key = Constants.SECRET
        setting.value = GoogleAuthenticator.generateSecretKey()
        save setting
        return Future.succeededFuture(setting.value)
      }
      return Future.succeededFuture(ar.value)
    }
  }

}
