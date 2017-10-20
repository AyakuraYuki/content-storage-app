package cc.ayakurayuki.contentstorage.utils

import org.apache.commons.lang3.StringUtils
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder

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
            byte[] bytes = encryptByte(data.getBytes(), KEY.getBytes())
            BASE64Encoder.class.newInstance().encode(bytes)
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
            byte[] dataBuf = BASE64Decoder.class.newInstance().decodeBuffer(data)
            byte[] bytes = decryptByte(dataBuf, KEY.getBytes())
            new String(bytes)
        }
        catch (Exception e) {
            e.getMessage()
        }
    }

}
