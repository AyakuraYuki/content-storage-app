package cc.ayakurayuki.contentstorage.common.base

import cc.ayakurayuki.contentstorage.common.util.EncodeUtils

/**
 * Created by ayakurayuki on 2018/1/16-11:21. <br/>
 * Package: cc.ayakurayuki.contentstorage.common.base <br/>
 */
class BaseBean {

  final static String SECRET = 'secret'
  final static String EMERGENCY = 'emergency'
  final static String AUTHENTIC = 'authentic'

  static String decodeBase64(String jsonData) {
    EncodeUtils.decodeBase64String(jsonData)
  }

  static String encodeBase64(String jsonData) {
    EncodeUtils.encodeBase64(jsonData)
  }

}
