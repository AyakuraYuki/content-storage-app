package cc.ayakurayuki.contentstorage.common.base

import cc.ayakurayuki.contentstorage.common.util.DESUtils

/**
 * Created by ayakurayuki on 2018/1/16-11:21. <br/>
 * Package: cc.ayakurayuki.contentstorage.common.base <br/>
 */
class BaseBean {

  final static def SECRET = 'secret'
  final static def EMERGENCY = 'emergency'
  final static def AUTHENTIC = 'authentic'

  final static def ROOT_PATH = 'redirect:/'

  static String decodeJSON(String jsonData) {
    // EncodeUtils.decodeBase64String(jsonData)
    DESUtils.decrypt(jsonData)
  }

  static String encodeJSON(String jsonData) {
    // EncodeUtils.encodeBase64(jsonData)
    DESUtils.encrypt(jsonData)
  }

}
