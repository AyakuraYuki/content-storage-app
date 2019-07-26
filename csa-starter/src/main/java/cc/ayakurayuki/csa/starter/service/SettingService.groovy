package cc.ayakurayuki.csa.starter.service

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.Setting
import cc.ayakurayuki.csa.starter.core.exception.StatusCodeException
import cc.ayakurayuki.csa.starter.core.util.GoogleAuthenticator
import cc.ayakurayuki.csa.starter.core.util.IDUtils
import cc.ayakurayuki.csa.starter.dao.SettingDao
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

/**
 *
 * @author ayakurayuki* @date 2019/05/21-11:01
 */
class SettingService extends BaseService {

  private def logger = LoggerFactory.getLogger getClass()
  private SettingDao settingDao

  SettingService() {
    settingDao = Constants.injector.getInstance SettingDao.class
  }

  /**
   * Get setting by using key name.<br>
   *
   * This function supports the following usage:<br>
   *
   * <ul>
   *   <li><pre>this["keyName"]</pre></li>
   *   <li><pre>settingService["keyName"]</pre></li>
   *   <li><pre>settingService.getAt("keyName")</pre></li>
   * </ul>
   *
   * @param key
   * @return
   */
  Future<Setting> getAt(String key) {
    Future.<JsonObject> future { f ->
      settingDao.getAt key, f
    }.compose { ar ->
      if (ar == null) {
        return Future.<Setting> succeededFuture()
      }
      def json = ar.encodePrettily()
      return Future.succeededFuture(Json.decodeValue(json, Setting.class))
    }
  }

  Future<Integer> save(Setting setting) {
    Future.<Integer> future { f ->
      settingDao.save setting, f
    } .compose { ar ->
      return Future.succeededFuture(ar)
    }
  }

  Future<Object> delete(String key) {
    Future.<Integer> future { f ->
      settingDao.delete key, f
    }.compose { ar ->
      if (ar > 0) {
        return Future.succeededFuture(ar)
      } else {
        return Future.failedFuture('No one has been deleted.')
      }
    }
  }

  /**
   * To initialize the Google 2FA.
   * @return
   */
  private Future<String> getSecretKey(boolean isReset) {
    this[Constants.SECRET].compose { ar ->
      if (isReset || null == ar) {
        def setting = [
            'id'   : IDUtils.UUID(),
            'key'  : Constants.SECRET,
            'value': GoogleAuthenticator.generateSecretKey()
        ] as Setting
        save setting
        return Future.succeededFuture(setting.value)
      }
      return Future.succeededFuture(ar.value)
    }
  }

  /**
   * To get 2FA QR code.
   * @param conditionCode condition code
   * @param isReset reset 2FA or not
   * @return
   */
  Future<String> getQrCode(String conditionCode, boolean isReset) {
    getSecretKey(isReset).compose { key ->
      Future.succeededFuture(GoogleAuthenticator.getQrCode(conditionCode, key))
    }
  }

  Future<Boolean> validateAuthCode(String authCode) {
    if (!StringUtils.isNumeric(authCode)) {
      return Future.succeededFuture(false)
    }
    def code = Long.valueOf authCode
    def authenticator = new GoogleAuthenticator()
    // This is an optional config, the default value is 3 (seconds).
    authenticator.windowSize = 5
    return this[Constants.SECRET].compose { ar ->
      if (null == ar) {
        throw new StatusCodeException(Constants.ErrorCode.TOTP_HAS_NOT_BEEN_INITIALIZED, 'Your 2FA has not been initialized.')
      }
      return Future.succeededFuture(authenticator.checkCode(ar.value, code))
    }
  }



}
