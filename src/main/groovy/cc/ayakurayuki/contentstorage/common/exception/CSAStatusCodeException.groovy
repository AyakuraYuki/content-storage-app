package cc.ayakurayuki.contentstorage.common.exception

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:50
 */
class CSAStatusCodeException extends CSAException {

  CSAStatusCodeException(int status, String message) {
    super(status, message)
  }

}
