package cc.ayakurayuki.contentstorage.common.exception

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:14
 */
class CSAAuthException extends CSAException {

  CSAAuthException(String message) {
    super(ReturnCode.AUTH_FAILED.code, message)
  }

  CSAAuthException(int status, String message) {
    super(status, message)
  }

}
