package cc.ayakurayuki.csa.starter.core.exception

import cc.ayakurayuki.csa.starter.core.config.Constants.ErrorCode

/**
 *
 * @author ayakurayuki* @date 2019/07/15-10:33
 */
class StatusCodeException extends RuntimeException {

  int status
  String message

  StatusCodeException(int status, String message) {
    this.status = status
    this.message = message
  }

  StatusCodeException(ErrorCode errorCode, String message) {
    this.status = errorCode.code
    this.message = message
  }

}
