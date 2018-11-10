package cc.ayakurayuki.contentstorage.common.base

import cc.ayakurayuki.contentstorage.common.util.DESUtils

/**
 * Created by ayakurayuki on 2018/1/16-11:21. <br/>
 * Package: cc.ayakurayuki.contentstorage.common.base <br/>
 */
class Base {

  final static def STRING_EMPTY = ''
  final static def SECRET = 'secret'
  final static def EMERGENCY = 'emergency'
  final static def AUTHENTIC = 'authentic'
  final static def DES_KEY = 'desKey'

  final static def ROOT_PATH = 'redirect:/content/'

  enum ResponseCode {
    NULL(-1000),
    ERROR(-1),
    OK(0)

    int code

    ResponseCode(int code) {
      this.code = code
    }
  }

  enum ErrorCode {
    AUTH_FAILED(-10000),
    ALL_EMERGENCY_CODE_USED(-10001),
    DES_DECRYPT_ERROR(-10002),
    DES_ENCRYPT_ERROR(-10003),
    OTHERS(-500)

    final int code

    ErrorCode(int code) {
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
