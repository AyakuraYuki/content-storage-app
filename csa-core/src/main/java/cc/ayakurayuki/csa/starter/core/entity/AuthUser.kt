package cc.ayakurayuki.csa.starter.core.entity

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User

/**
 *
 * @author ayakurayuki
 * @date 2019/05/21-11:37
 */
data class AuthUser(val authInfo: JsonObject) : User {

  override fun clearCache(): User? {
    // Do nothing...
    return null
  }

  override fun setAuthProvider(authProvider: AuthProvider?) {
    // Do nothing...
  }

  override fun isAuthorized(authority: String?, resultHandler: Handler<AsyncResult<Boolean>>?): User {
    resultHandler?.handle(Future.succeededFuture(true))
    return this
  }

  override fun principal(): JsonObject {
    return authInfo
  }

  override fun toString(): String {
    return Json.encode(this)
  }

}
