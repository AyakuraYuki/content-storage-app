package cc.ayakurayuki.csa.starter.core.util

import cc.ayakurayuki.csa.starter.core.util.security.EncryptUtils

import java.security.SecureRandom

/**
 *
 * @author ayakurayuki* @date 2019/05/21-10:59
 */
final class RandomUtils {

  def static random = new SecureRandom()

  private static final def CHARACTERS = "abcdefghijklmnopqrstuvwxyz"
  private static final def NUMBERS = "0123456789"
  private static final def KEY_NODE = "${NUMBERS}${CHARACTERS}".toCharArray()
  private static final def KEY_NODE_WITHOUT_NUMBERS = "${CHARACTERS}".toCharArray()

  static String getEmergencyKey() {
    def baseData = IDUtils.UUID()
    def data = "blob ${baseData.length()}-${baseData}-${System.currentTimeMillis()}\0"
    def key = EncryptUtils.SHA1(data)
    return key.substring(0, 7).toUpperCase()
  }

  static randomLong() {
    Math.abs(random.nextLong())
  }

  static randomBase62(int length) {
    def randomBytes = new byte[length]
    random.nextBytes(randomBytes)
    EncodeUtils.encodeBase62(randomBytes)
  }

}
