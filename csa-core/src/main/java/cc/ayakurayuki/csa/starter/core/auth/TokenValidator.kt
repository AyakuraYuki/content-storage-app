package cc.ayakurayuki.csa.starter.core.auth;

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AbstractUser
import io.vertx.ext.auth.AuthProvider

class TokenValidator(private val token: String, val errCode: Int, val errMsg: String) : AbstractUser() {

  override fun doIsPermitted(permission: String?, resultHandler: Handler<AsyncResult<Boolean>>?) {
    resultHandler?.handle(Future.succeededFuture(errCode == 0))
  }

  override fun setAuthProvider(authProvider: AuthProvider?) {
    // not utilized by Rest.vertx
  }

  override fun principal(): JsonObject {
    return JsonObject().apply {
      put("token", token)
      put("errCode", 0)
      put("errMsg", "")
    }
  }

}
