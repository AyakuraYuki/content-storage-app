package cc.ayakurayuki.contentstorage.common.exception

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:50
 */
class StatusCodeException extends CSAException {

  StatusCodeException(int status, String message) {
    super(status, message)
  }

}
