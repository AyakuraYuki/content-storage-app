package cc.ayakurayuki.contentstorage.common.exception

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:15
 */
class CSAException extends RuntimeException {

  int status

  CSAException() { super }

  CSAException(String message) {
    super(message)
  }

  CSAException(int status, String message) {
    super(message)
    this.status = status
  }

  CSAException(Throwable throwable) {
    super(throwable)
  }

  CSAException(String message, Throwable throwable) {
    super(message, throwable)
  }

  CSAException(int status, String message, Throwable throwable) {
    super(message, throwable)
    this.status = status
  }

}
