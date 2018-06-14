package cc.ayakurayuki.contentstorage.common.util

import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang3.StringUtils

import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import java.security.SecureRandom

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
class DESUtils {

  private final static String DES = "DES"
  public static final String KEY = "F3EA9825E5BF4964AAE2755E9090A0D2-1286816A24184994B4C8A32B1285A08E"

  static SecureRandom random
  static SecretKeyFactory keyFactory
  static Cipher cipher

  static {
    random = new SecureRandom()
    keyFactory = SecretKeyFactory.getInstance(DES)
    cipher = Cipher.getInstance(DES)
  }

  private static byte[] encryptByte(byte[] data, byte[] key) {
    def desKey = new DESKeySpec(key)
    def secretKey = keyFactory.generateSecret(desKey)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, random)
    cipher.doFinal(data)
  }

  private static byte[] decryptByte(byte[] data, byte[] key) {
    def desKey = new DESKeySpec(key)
    def secretKey = keyFactory.generateSecret(desKey)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, random)
    cipher.doFinal(data)
  }

  /**
   * 加密数据
   * @param data 原始数据
   * @return 加密密文
   */
  static String encrypt(String data) {
    if (data == null) {
      StringUtils.EMPTY
    }
    try {
      byte[] bytes = encryptByte(data.bytes, KEY.bytes)
      new String(new Base64().encode(bytes))
    }
    catch (Exception e) {
      e.getMessage()
    }
  }

  /**
   * 解密密文
   * @param data 密文数据
   * @return 解密输出
   */
  static String decrypt(String data) {
    if (data == null) {
      StringUtils.EMPTY
    }
    try {
      byte[] dataBuf = new Base64().decode(data.bytes)
      byte[] bytes = decryptByte(dataBuf, KEY.bytes)
      new String(bytes)
    }
    catch (Exception e) {
      e.getMessage()
    }
  }

}
