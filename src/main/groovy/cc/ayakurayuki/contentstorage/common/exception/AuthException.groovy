package cc.ayakurayuki.contentstorage.common.exception

import cc.ayakurayuki.contentstorage.common.base.Base

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:14
 */
class AuthException extends CSAException {

  AuthException(String message) {
    super(Base.ErrorCode.AUTH_FAILED.code, message)
  }

  AuthException(int status, String message) {
    super(status, message)
  }

}
