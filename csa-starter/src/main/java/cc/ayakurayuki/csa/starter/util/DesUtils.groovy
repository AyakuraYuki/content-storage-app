package cc.ayakurayuki.csa.starter.util

import cc.ayakurayuki.csa.starter.core.config.Constants
import cc.ayakurayuki.csa.starter.core.exception.StatusCodeException
import cc.ayakurayuki.csa.starter.service.SettingService
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import org.apache.commons.codec.binary.Base64

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec
import java.security.SecureRandom
import java.sql.DriverManager

/**
 *
 * @author ayakurayuki* @date 2019/05/21-10:48
 */
final class DesUtils {

  private static final String TRIPLE_DES = "DESede"
  private static final SecureRandom random = new SecureRandom()
  private static final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(TRIPLE_DES)
  private static final Cipher cipher = Cipher.getInstance(TRIPLE_DES)

  private static SettingService settingService

  private static Future<String> getKey() {
    if (settingService == null) {
      settingService = Constants.injector.getInstance SettingService.class
    }
    settingService[Constants.DES_KEY].compose { ar -> Future.succeededFuture ar.value }
  }

  private static String keyFromJDBC() {
    try {
      JsonObject config = Constants.context.config()
      Class.forName config.getString('db.driver', 'org.sqlite.JDBC')
      def url = config.getString 'db.url', 'jdbc:sqlite:data/storage.db'
      def conn = DriverManager.getConnection url
      final def sql = 'SELECT * FROM settings WHERE key = ?'
      def stat = conn.prepareStatement sql
      stat.setObject 1, Constants.DES_KEY
      def result = stat.executeQuery()
      if (result.next()) {
        final def value = result.getString 'value'
        result.close()
        stat.close()
        conn.close()
        return value
      }
    } catch (Exception e) {
      throw new StatusCodeException(Constants.ErrorCode.OTHERS, e.localizedMessage)
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

  // region Traditional encrypt and decrypt

  private static byte[] encryptByteOld(byte[] data, byte[] key) {
    def desKey = new DESedeKeySpec(key)  // new DESKeySpec(key)
    def secretKey = keyFactory.generateSecret desKey
    cipher.init Cipher.ENCRYPT_MODE, secretKey, random
    cipher.doFinal data
  }

  private static byte[] decryptByteOld(byte[] data, byte[] key) {
    def desKey = new DESedeKeySpec(key)  // new DESKeySpec(key)
    def secretKey = keyFactory.generateSecret desKey
    cipher.init Cipher.DECRYPT_MODE, secretKey, random
    cipher.doFinal data
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
      byte[] bytes = encryptByteOld data.getBytes('UTF-8'), keyFromJDBC().bytes
      return new String(new Base64().encode(bytes))
    }
    catch (Exception e) {
      throw new StatusCodeException(Constants.ErrorCode.DES_ENCRYPT_ERROR.code, "Encrypt error: ${e.localizedMessage} [${data}]".toString())
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
      byte[] bytes = decryptByteOld dataBuf, keyFromJDBC().bytes
      return new String(bytes, 'UTF-8')
    }
    catch (Exception e) {
      throw new StatusCodeException(Constants.ErrorCode.DES_DECRYPT_ERROR.code, "Decrypt error: ${e.localizedMessage}".toString())
    }
  }

  // endregion

}
