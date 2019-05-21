package cc.ayakurayuki.csa.starter.util

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.Setting
import cc.ayakurayuki.csa.starter.core.exception.DESException
import cc.ayakurayuki.csa.starter.core.util.IDUtils
import cc.ayakurayuki.csa.starter.service.SettingService
import io.vertx.core.Future
import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang3.StringUtils

import javax.annotation.PostConstruct
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec
import java.security.SecureRandom

/**
 *
 * @author ayakurayuki* @date 2019/05/21-10:48
 */
final class DESUtils {

  private SettingService settingService
  private static DESUtils desUtils

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
    desUtils.settingService = this.settingService
    def desKey = desUtils.settingService[Constants.DES_KEY] as Setting
    if (desKey == null) {
      desKey = [
          id   : IDUtils.UUID(),
          key  : Constants.DES_KEY,
          value: "${IDUtils.UUID()}${IDUtils.UUID()}".toString()
      ] as Setting
      desUtils.settingService.save(desKey)
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
      return Constants.EMPTY
    }
    try {
      byte[] bytes = encryptByte(data.getBytes('UTF-8'), KEY.bytes)
      return new String(new Base64().encode(bytes))
    }
    catch (Exception e) {
      throw new DESException(Constants.ErrorCode.DES_ENCRYPT_ERROR.code, "Encrypt error: ${e.localizedMessage} [${data}]".toString())
    }
  }

  /**
   * 解密密文
   * @param data 密文数据
   * @return 解密输出
   */
  static String decrypt(String data) {
    if (data == null) {
      return Constants.EMPTY
    }
    try {
      byte[] dataBuf = new Base64().decode(data.bytes)
      byte[] bytes = decryptByte(dataBuf, KEY.bytes)
      return new String(bytes, 'UTF-8')
    }
    catch (Exception e) {
      throw new DESException(Constants.ErrorCode.DES_DECRYPT_ERROR.code, "Decrypt error: ${e.localizedMessage}".toString())
    }
  }

  static Future<String> decryptFuture(String data) {
    Future.<String> future { f ->
      if (StringUtils.isEmpty(data)) {
        return Future.succeededFuture(Constants.EMPTY)
      }
      try {
        byte[] dataBuf = new Base64().decode(data.bytes)
        byte[] bytes = decryptByte(dataBuf, KEY.bytes)
        f.complete new String(bytes, 'UTF-8')
      } catch (Exception e) {
        f.fail(e.localizedMessage)
      }
    }
  }

}
