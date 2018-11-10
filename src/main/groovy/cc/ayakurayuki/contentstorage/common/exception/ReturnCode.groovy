package cc.ayakurayuki.contentstorage.common.exception

/**
 *
 * @author ayakurayuki
 * @date 2018/11/10-16:05
 */
enum ReturnCode {

  AUTH_FAILED(-10000),
  ALL_EMERGENCY_CODE_USED(-10001),
  OTHERS(-500)

  final int code

  ReturnCode(int code) {
    this.code = code
  }

}
