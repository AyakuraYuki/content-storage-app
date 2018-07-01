package cc.ayakurayuki.contentstorage.common.base

import cc.ayakurayuki.contentstorage.common.util.DESUtils
import org.apache.logging.log4j.LogManager

/**
 * Created by ayakurayuki on 2018/1/16-11:21. <br/>
 * Package: cc.ayakurayuki.contentstorage.common.base <br/>
 */
class BaseBean {

  final static def STRING_EMPTY = ''
  final static def SECRET = 'secret'
  final static def EMERGENCY = 'emergency'
  final static def AUTHENTIC = 'authentic'

  final static def ROOT_PATH = 'redirect:/'

  enum RESPONSE_RESULT {
    NULL(-1000),
    ERROR(-1),
    OK(0)

    int code

    RESPONSE_RESULT(int code) {
      this.code = code
    }
  }

  static String decodeJSON(String jsonData) {
    // EncodeUtils.decodeBase64String(jsonData)
    DESUtils.decrypt(jsonData)
  }

  static String encodeJSON(String jsonData) {
    // EncodeUtils.encodeBase64(jsonData)
    DESUtils.encrypt(jsonData)
  }

}
