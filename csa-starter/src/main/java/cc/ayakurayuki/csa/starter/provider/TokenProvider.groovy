package cc.ayakurayuki.csa.starter.provider

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.AuthUser
import cc.ayakurayuki.csa.starter.core.entity.Setting
import cc.ayakurayuki.csa.starter.service.SettingService
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User

/**
 * Token 校验
 * @author ayakurayuki* @date 2019/05/21-10:27
 */
class TokenProvider implements AuthProvider {

  private final SettingService service

  TokenProvider() {
    service = Constants.injector.getInstance SettingService.class
  }

  @Override
  void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
    def token = authInfo.getString 'token'
    def storedToken = service[Constants.TOKEN] as Setting
    if (storedToken.value == token) {
      def user = new AuthUser(authInfo)
      resultHandler.handle Future.succeededFuture(user)
    } else {
      resultHandler.handle Future.failedFuture('Token invalidated!')
    }
  }

}
