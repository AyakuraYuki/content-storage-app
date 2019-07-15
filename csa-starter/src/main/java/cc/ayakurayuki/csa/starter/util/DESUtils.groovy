package cc.ayakurayuki.csa.starter.util

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.entity.Setting
import cc.ayakurayuki.csa.starter.core.util.IDUtils
import cc.ayakurayuki.csa.starter.service.SettingService
import io.vertx.core.Future
import org.apache.commons.codec.binary.Base64

import javax.crypto.Cipher
import javax.crypto.SecretKey
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
    Future.<DESedeKeySpec> future { f -> f.complete new DESedeKeySpec(key) }
        .compose { desKey -> Future.<SecretKey> succeededFuture keyFactory.generateSecret(desKey) }
        .compose { secretKey ->
          cipher.init Cipher.ENCRYPT_MODE, secretKey, random
          Future.<byte[]> succeededFuture cipher.doFinal(data)
        }
  }

  private static Future<byte[]> decryptByte(byte[] data, byte[] key) {
    Future.<DESedeKeySpec> future { f -> f.complete new DESedeKeySpec(key) }
        .compose { desKey -> Future.<SecretKey> succeededFuture keyFactory.generateSecret(desKey) }
        .compose { secretKey ->
          cipher.init Cipher.DECRYPT_MODE, secretKey, random
          Future.<byte[]> succeededFuture cipher.doFinal(data)
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
            .compose { ar2 -> Future.<String> succeededFuture new String(new Base64().encode(ar2)) }
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
            .compose { ar2 -> Future.<String> succeededFuture new String(ar2, 'UTF-8') }
      } catch (Exception e) {
        return Future.<String> future { f -> f.fail e.localizedMessage }
      }
    }
  }

}