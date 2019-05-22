package cc.ayakurayuki.csa.starter.util

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.Setting
import cc.ayakurayuki.csa.starter.core.util.IDUtils
import cc.ayakurayuki.csa.starter.service.SettingService
import io.vertx.core.Future
import org.apache.commons.codec.binary.Base64

import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec
import java.security.SecureRandom

/**
 *
 * @author ayakurayuki* @date 2019/05/21-10:48
 */
final class DESUtils {

  private static final String TRIPLE_DES = "DESede"
  private static final SecureRandom random = new SecureRandom()
  private static final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(TRIPLE_DES)
  private static final Cipher cipher = Cipher.getInstance(TRIPLE_DES)

  private static SettingService settingService

  private static Future<String> getKey() {
    if (settingService == null) {
      settingService = Constants.injector.getInstance SettingService.class
    }
    settingService[Constants.DES_KEY]
        .compose { ar ->
          if (ar != null) {
            return Future.succeededFuture(ar.value)
          } else {
            def desKey = [
                id   : IDUtils.UUID(),
                key  : Constants.DES_KEY,
                value: "${IDUtils.UUID()}${IDUtils.UUID()}".toString()
            ] as Setting
            settingService.save desKey
            return Future.succeededFuture(desKey.value)
          }
        }
  }

  private static Future<byte[]> encryptByte(byte[] data, byte[] key) {
    Constants.<byte[]> executeBlocking { f ->
      def desKey = new DESedeKeySpec(key)
      def secretKey = keyFactory.generateSecret desKey
      cipher.init Cipher.ENCRYPT_MODE, secretKey, random
      f.complete cipher.doFinal(data)
    }
  }

  private static Future<byte[]> decryptByte(byte[] data, byte[] key) {
    Constants.<byte[]> executeBlocking { f ->
      def desKey = new DESedeKeySpec(key)
      def secretKey = keyFactory.generateSecret desKey
      cipher.init Cipher.DECRYPT_MODE, secretKey, random
      f.complete cipher.doFinal(data)
    }
  }

  /**
   * 加密数据
   * @param data 原始数据
   * @return 加密密文
   */
  static Future<String> encryptFuture(String data) {
    if (data == null) {
      return Future.<String> succeededFuture(Constants.EMPTY)
    }
    key.compose { ar ->
      try {
        return encryptByte(data.getBytes('UTF-8'), ar.bytes)
            .compose { ar2 ->
              Future.<String> future { f ->
                f.complete new String(new Base64().encode(ar2))
              }
            }
      } catch (Exception e) {
        return Future.<String> future { f -> f.fail e.localizedMessage }
      }
    }
  }

  /**
   * 解密密文
   * @param data 密文数据
   * @return 解密输出
   */
  static Future<String> decryptFuture(String data) {
    if (data == null) {
      return Future.<String> succeededFuture(Constants.EMPTY)
    }
    key.compose { ar ->
      try {
        return Constants.<byte[]> executeBlocking { f -> f.complete new Base64().decode(data.bytes) }
            .compose { buf -> decryptByte buf, ar.bytes }
            .compose { ar2 ->
              Future.<String> future { f ->
                f.complete new String(ar2, 'UTF-8')
              }
            }
      } catch (Exception e) {
        return Future.<String> future { f -> f.fail e.localizedMessage }
      }
    }
  }

  // region Traditional encrypt and decrypt

//  private static byte[] encryptByteOld(byte[] data, byte[] key) {
//    def desKey = new DESedeKeySpec(key)  // new DESKeySpec(key)
//    def secretKey = keyFactory.generateSecret desKey
//    cipher.init Cipher.ENCRYPT_MODE, secretKey, random
//    cipher.doFinal data
//  }

//  private static byte[] decryptByteOld(byte[] data, byte[] key) {
//    def desKey = new DESedeKeySpec(key)  // new DESKeySpec(key)
//    def secretKey = keyFactory.generateSecret desKey
//    cipher.init Cipher.DECRYPT_MODE, secretKey, random
//    cipher.doFinal data
//  }

//  /**
//   * 加密数据
//   * @param data 原始数据
//   * @return 加密密文
//   */
//  static String encrypt(String data) {
//    if (data == null) {
//      return Constants.EMPTY
//    }
//    try {
//      byte[] bytes = encryptByteOld data.getBytes('UTF-8'), key.result().bytes
//      return new String(new Base64().encode(bytes))
//    }
//    catch (Exception e) {
//      throw new DESException(Constants.ErrorCode.DES_ENCRYPT_ERROR.code, "Encrypt error: ${e.localizedMessage} [${data}]".toString())
//    }
//  }

//  /**
//   * 解密密文
//   * @param data 密文数据
//   * @return 解密输出
//   */
//  static String decrypt(String data) {
//    if (data == null) {
//      return Constants.EMPTY
//    }
//    try {
//      byte[] dataBuf = new Base64().decode(data.bytes)
//      byte[] bytes = decryptByteOld dataBuf, key.result().bytes
//      return new String(bytes, 'UTF-8')
//    }
//    catch (Exception e) {
//      throw new DESException(Constants.ErrorCode.DES_DECRYPT_ERROR.code, "Decrypt error: ${e.localizedMessage}".toString())
//    }
//  }

  // endregion

}
