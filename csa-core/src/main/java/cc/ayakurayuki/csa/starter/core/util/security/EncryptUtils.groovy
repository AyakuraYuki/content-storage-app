package cc.ayakurayuki.csa.starter.core.util.security

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import static java.lang.System.err

/**
 *
 * @author ayakurayuki* @date 2019/05/21-10:46
 */
final class EncryptUtils {

  private final static def HEX = '0123456789ABCDEF'.toCharArray()

  /**
   * @param message : content
   * @return byte[] : raw content
   */
  private static byte[] GetEncodeByte(String message, MessageTypeEnum type) {
    try {
      def messageDigest = MessageDigest.getInstance(type.type)
      messageDigest.update(message.bytes)
      return messageDigest.digest()
    } catch (NoSuchAlgorithmException ignored) {
      err.println("Type ${type.name()} is not support.")
      return null
    }
  }

  /**
   * @param raw : content
   * @return encrypt password
   */
  private static String ConvertToString(byte[] raw) {
    def stringBuffer = new StringBuffer()
    raw.each { b ->
      stringBuffer.append(HEX[(b >>> 4) & 0x0f])
      stringBuffer.append(HEX[b & 0x0f])
    }
    return stringBuffer.toString().toLowerCase()
  }

  /**
   * 10 times loop encrypt by md5.
   *
   * @param message
   * @return
   */
  static String MD5ForTenTimes(String message) {
    def result = message
    for (_ in 1..10) {
      result = ConvertToString(GetEncodeByte(result, MessageTypeEnum.MD5))
    }
    return result
  }

  /**
   * encrypt by md5.
   *
   * @param message
   * @return
   */
  static String MD5(String message) {
    return ConvertToString(GetEncodeByte(message, MessageTypeEnum.MD5))
  }

  /**
   * 10 times loop encrypt by sha-1.
   *
   * @param message
   * @return
   */
  static String SHA1ForTenTimes(String message) {
    String result = message
    for (_ in 1..10) {
      result = ConvertToString(GetEncodeByte(result, MessageTypeEnum.SHA1))
    }
    return result
  }

  /**
   * encrypt by sha-1.
   *
   * @param message
   * @return
   */
  static String SHA1(String message) {
    return ConvertToString(GetEncodeByte(message, MessageTypeEnum.SHA1))
  }

  /**
   * 10 times loop encrypt by sha-256.
   *
   * @param message
   * @return
   */
  static String SHA256ForTenTimes(String message) {
    String result = message
    for (_ in 1..10) {
      result = ConvertToString(GetEncodeByte(result, MessageTypeEnum.SHA256))
    }
    return result
  }

  /**
   * encrypt by sha-256.
   *
   * @param message
   * @return
   */
  static String SHA256(String message) {
    return ConvertToString(GetEncodeByte(message, MessageTypeEnum.SHA256))
  }

}
