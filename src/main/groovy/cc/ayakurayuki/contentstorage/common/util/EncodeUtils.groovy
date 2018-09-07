package cc.ayakurayuki.contentstorage.common.util

import org.apache.commons.codec.DecoderException
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Hex
import org.apache.commons.text.StringEscapeUtils

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
class EncodeUtils {

  private static final String DEFAULT_URL_ENCODING = "UTF-8"
  private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()

  /**
   * Hex编码
   *
   * @param input
   * @return
   */
  static String encodeHex(byte[] input) {
    return new String(Hex.encodeHex(input))
  }

  /**
   * Hex解码
   *
   * @param input
   * @return
   */
  static byte[] decodeHex(String input) {
    try {
      return Hex.decodeHex(input.toCharArray())
    } catch (DecoderException e) {
      throw new RuntimeException(e)
    }
  }

  /**
   * Base64编码
   *
   * @param input
   * @return
   */
  static String encodeBase64(byte[] input) {
    return new String(Base64.encodeBase64(input))
  }

  /**
   * Base64编码
   *
   * @param input
   * @return
   */
  static String encodeBase64(String input) {
    try {
      return new String(Base64.encodeBase64(input.getBytes(DEFAULT_URL_ENCODING)))
    } catch (UnsupportedEncodingException ignored) {
      return ""
    }
  }

  /**
   * Base64解码
   *
   * @param input
   * @return
   */
  static byte[] decodeBase64(String input) {
    return Base64.decodeBase64(input.getBytes())
  }

  /**
   * Base64解码
   *
   * @param input
   * @return
   */
  static String decodeBase64String(String input) {
    try {
      return new String(Base64.decodeBase64(input.getBytes()), DEFAULT_URL_ENCODING)
    } catch (UnsupportedEncodingException ignored) {
      return ""
    }
  }

  /**
   * Base62编码
   *
   * @param input
   * @return
   */
  static String encodeBase62(byte[] input) {
    char[] chars = new char[input.length]
    for (int i = 0; i < input.length; i++) {
      chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)]
    }
    return new String(chars)
  }

  /**
   * Html转码
   *
   * @param html
   * @return
   */
  static String escapeHtml(String html) {
    return StringEscapeUtils.escapeHtml4(html)
  }

  /**
   * Html解码
   *
   * @param htmlEscaped
   * @return
   */
  static String unescapeHtml(String htmlEscaped) {
    return StringEscapeUtils.unescapeHtml4(htmlEscaped)
  }

  /**
   * Xml转码
   *
   * @param xml
   * @return
   */
  static String escapeXml(String xml) {
    return StringEscapeUtils.escapeXml10(xml)
  }

  /**
   * Xml解码
   *
   * @param xmlEscaped
   * @return
   */
  static String unescapeXml(String xmlEscaped) {
    return StringEscapeUtils.unescapeXml(xmlEscaped)
  }

  /**
   * URL编码,默认UTF-8
   *
   * @param part
   * @return
   */
  static String urlEncode(String part) {
    try {
      return URLEncoder.encode(part, DEFAULT_URL_ENCODING)
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e)
    }
  }

  /**
   * URL解码,默认UTF-8
   *
   * @param part
   * @return
   */
  static String urlDecode(String part) {
    try {
      return URLDecoder.decode(part, DEFAULT_URL_ENCODING)
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e)
    }
  }

}
