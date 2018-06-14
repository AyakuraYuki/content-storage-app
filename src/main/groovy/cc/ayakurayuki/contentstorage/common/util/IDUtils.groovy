package cc.ayakurayuki.contentstorage.common.util

import java.security.SecureRandom

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
class IDUtils {

  def static random = new SecureRandom()

  def static UUID() {
    UUID.randomUUID().toString().replaceAll("-", "").toUpperCase()
  }

  def static randomLong() {
    Math.abs(random.nextLong())
  }

  def static randomBase62(int length) {
    def randomBytes = new byte[length]
    random.nextBytes(randomBytes)
    EncodeUtils.encodeBase62(randomBytes)
  }

}
