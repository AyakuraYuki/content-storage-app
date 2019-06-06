package cc.ayakurayuki.csa.starter.core.exception

/**
 *
 * @author ayakurayuki* @date 2019/06/06-15:20
 */
class AuthException extends RuntimeException {

  int status
  String message

  AuthException(int status, String message) {
    this.status = status
    this.message = message
  }

}
