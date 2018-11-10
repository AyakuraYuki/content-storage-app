package cc.ayakurayuki.contentstorage.common.util

import cc.ayakurayuki.contentstorage.common.base.Base
import cc.ayakurayuki.contentstorage.common.exception.DESException
import cc.ayakurayuki.contentstorage.module.settings.entity.Settings
import cc.ayakurayuki.contentstorage.module.settings.service.SettingsService
import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec
import java.security.SecureRandom

/**
 * Created by Ayakura Yuki on 2017/9/30.
 */
@Component
final class DESUtils {

  @Autowired
  private SettingsService settingsService
  private static DESUtils desUtils

  private final static String DES = "DES"
  private final static String TRIPLE_DES = "DESede"
  public static String KEY

  static SecureRandom random
  static SecretKeyFactory keyFactory
  static Cipher cipher

  static {
    random = new SecureRandom()
    keyFactory = SecretKeyFactory.getInstance(TRIPLE_DES)
    cipher = Cipher.getInstance(TRIPLE_DES)
  }

  @PostConstruct
  void init() {
    desUtils = this
    desUtils.settingsService = this.settingsService
    def desKey = desUtils.settingsService.getByKey(Base.DES_KEY)
    if (desKey == null) {
      desKey = [
          id   : IDUtils.UUID(),
          key  : Base.DES_KEY,
          value: "${IDUtils.UUID()}${IDUtils.UUID()}".toString()
      ] as Settings
      desUtils.settingsService.save(desKey)
    }
    KEY = desKey.value
  }

  private static byte[] encryptByte(byte[] data, byte[] key) {
    def desKey = new DESedeKeySpec(key)  // new DESKeySpec(key)
    def secretKey = keyFactory.generateSecret(desKey)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, random)
    cipher.doFinal(data)
  }

  private static byte[] decryptByte(byte[] data, byte[] key) {
    def desKey = new DESedeKeySpec(key)  // new DESKeySpec(key)
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
      return Base.STRING_EMPTY
    }
    try {
      byte[] bytes = encryptByte(data.getBytes('UTF-8'), KEY.bytes)
      return new String(new Base64().encode(bytes))
    }
    catch (Exception e) {
      throw new DESException(
          Base.ErrorCode.DES_ENCRYPT_ERROR.code,
          "Encrypt error: ${e.localizedMessage} [${data}]".toString()
      )
    }
  }

  /**
   * 解密密文
   * @param data 密文数据
   * @return 解密输出
   */
  static String decrypt(String data) {
    if (data == null) {
      return Base.STRING_EMPTY
    }
    try {
      byte[] dataBuf = new Base64().decode(data.bytes)
      byte[] bytes = decryptByte(dataBuf, KEY.bytes)
      return new String(bytes, 'UTF-8')
    }
    catch (Exception e) {
      throw new DESException(
          Base.ErrorCode.DES_DECRYPT_ERROR.code,
          "Decrypt error: ${e.localizedMessage}".toString()
      )
    }
  }

}
