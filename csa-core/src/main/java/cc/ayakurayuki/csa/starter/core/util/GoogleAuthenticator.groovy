package cc.ayakurayuki.csa.starter.core.util

import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Base64

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

/**
 *
 * @author ayakurayuki* @date 2019/05/21-11:00
 */
final class GoogleAuthenticator {

  /**
   * 生成的key长度 Generate secret key length
   */
  public static final def SECRET_SIZE = 10

  /**
   * 随机密钥种子 The seed of random secret key generator
   */
  public static final def SEED = 'g8GjEvTbW5oVSV7avL47357438reyhreyuryetredLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx'

  /**
   * Java实现随机数算法 Random number algorithm, implement by Java
   */
  public static final def RANDOM_NUMBER_ALGORITHM = 'SHA1PRNG'

  /**
   * 最多可偏移的时间 Max window size, default: 3, max: 17
   */
  int windowSize = 3

  /**
   * Set the windows size. This is an integer value representing the number of
   * 30 second windows we allow The bigger the window, the more tolerant of
   * clock skew we are.
   *
   * @param s
   *  window size - must be >=1 and <=17. Other values are ignored
   */
  void setWindowSize(int s) {
    if (s >= 1 && s <= 17) {
      windowSize = s
    }
  }

  private static int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
    def data = new byte[8]
    def value = t
    for (int i = 8; i-- > 0; value >>>= 8) {
      data[i] = (byte) value
    }
    def mac = Mac.getInstance("HmacSHA1")
    def signKey = new SecretKeySpec(key, "HmacSHA1")
    mac.init(signKey)
    def hash = mac.doFinal(data)
    int offset = hash[20 - 1] & 0xF
    // We're using a long because Java hasn't got unsigned int.
    long truncatedHash = 0
    for (int i = 0; i < 4; ++i) {
      truncatedHash <<= 8
      // We are dealing with signed bytes:
      // we just keep the first byte.
      truncatedHash |= (hash[offset + i] & 0xFF)
    }
    truncatedHash &= 0x7FFFFFFF
    truncatedHash %= 1000000
    return (int) truncatedHash
  }

  /**
   * Generate a random secret key. This must be saved by the server and
   * associated with the users account to verify the code displayed by Google
   * Authenticator. The user must register this secret on their device.
   * <p>
   * 生成一个随机密钥, 这个密钥必须保存在服务器于用户两端, 鉴权时通过该密钥生成OTP并进行两端OTP对比
   *
   * @return secret key
   */
  static generateSecretKey() {
    try {
      def secureRandom = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM)
      secureRandom.setSeed(Base64.decodeBase64(SEED))
      def seedBytes = secureRandom.generateSeed(SECRET_SIZE)
      def base32 = new Base32()
      def encodedKeyBytes = base32.encode(seedBytes)
      def encodedKeyString = new String(encodedKeyBytes)
      return encodedKeyString
    } catch (NoSuchAlgorithmException ignored) {
    }
    return null
  }

  /**
   * Return a URL that generates and displays a QR barcode. The user scans
   * this bar code with the Google Authenticator application on their
   * smartphone to register the auth code. They can also manually enter the
   * secret if desired
   *
   * @param user
   *  user id (e.g. fflinstone)
   * @param host
   *  host or system that the code is for (e.g. myapp.com)
   * @param secret
   *  the secret that was previously generated for this user
   * @return the URL for the QR code to scan
   */
  static String getQrCodeUrl(String user, String host, String secret) {
    return "http://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/$user@$host?secret=$secret"
  }

  /**
   * 生成一个google身份验证器，识别的字符串，只需要把该方法返回值生成二维码扫描就可以了。
   *
   * @param user
   *  账号
   * @param secret
   *  密钥
   * @return
   */
  static String getQrCode(String user, String secret) {
    return "otpauth://totp/$user?secret=$secret"
  }

  /**
   * Check the code entered by the user to see if it is valid 验证code是否合法
   *
   * @param secret
   *  The users secret.
   * @param code
   *  The code displayed on the users device
   * @param t
   *  The time in msec (System.currentTimeMillis() for example)
   * @return
   */
  boolean checkCode(String secret, long code, long timeMsec) {
    def codec = new Base32()
    def decodedKey = codec.decode(secret)
    // convert unix msec time into a 30 second "window"
    // this is per the TOTP spec (see the RFC for details)
    def t = (timeMsec / 1000L) / 30L
    // Window is used to check codes generated in the near past.
    // You can use this value to tune how far you're willing to go.
    for (int i = -windowSize; i <= windowSize; ++i) {
      long hash
      try {
        hash = verifyCode(decodedKey, t.toLong() + i)
      } catch (Exception e) {
        // Yes, this is bad form - but
        // the exceptions thrown would be rare and a static
        // configuration problem
        e.printStackTrace()
        throw new RuntimeException(e.message)
        // return false;
      }
      if (hash == code) {
        return true
      }
    }
    // The validation code is invalid.
    return false
  }

  /**
   * Check the code entered by the user to see if it is valid 验证code是否合法
   *
   * @param secret
   *  The users secret.
   * @param code
   *  The code displayed on the users device
   * @return
   */
  boolean checkCode(String secret, long code) {
    this.checkCode(secret, code, System.currentTimeMillis())
  }

}
