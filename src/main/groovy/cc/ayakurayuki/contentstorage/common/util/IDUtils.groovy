package cc.ayakurayuki.contentstorage.common.util

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
class IDUtils {

  def static UUID() {
    UUID.randomUUID().toString().replaceAll("-", "").toUpperCase()
  }

}
