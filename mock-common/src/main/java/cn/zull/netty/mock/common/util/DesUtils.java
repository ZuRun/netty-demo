package cn.zull.netty.mock.common.util;

import cn.zull.netty.mock.common.constants.ErrorCode;
import cn.zull.netty.mock.common.global.CipherException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * @author songzhang5
 * @date 2019/1/23 21:11:26
 */
@Slf4j
public class DesUtils {
    private static final String ALGORITHM_MD5 = "md5";
    private static final String ALGORITHM_DESEDE = "DESede";
    private static final String CIPHER_TRANSFORMATION = "DESede/CBC/PKCS5Padding";
    private static final String CHARSET_UTF_8 = "UTF-8";

    /**
     * 加密
     *
     * @param message 待加密内容
     * @param sKey    密钥
     * @return 密文
     * @throws Exception
     * @see
     */
    public static String encrypt(String message, String sKey) {
        try {
            byte[] keyBytes = getKeyBytes(sKey);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(1, key, iv);
            byte[] plainTextBytes = message.getBytes("UTF-8");
            byte[] cipherText = cipher.doFinal(plainTextBytes);
            byte[] encoded = Base64.encode(cipherText);
            return StringUtils.newStringUtf8(encoded);
        } catch (Exception e) {
            log.error("[aes解密失败] key:{} msg:{} eName:{} eMsg:{}", sKey, message, e.getClass().getSimpleName(), e.getMessage());
            throw new CipherException(ErrorCode.cipher.CIPHER_INSTANCE);
        }
    }

    /**
     * 解密
     *
     * @param msg  密文
     * @param sKey 密钥
     * @return 明文
     * @throws Exception
     * @see
     */
    public static String decrypt(String msg, String sKey) {
        try {
            byte[] message = Base64.decode(msg);
            byte[] keyBytes = getKeyBytes(sKey);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decipher.init(2, key, iv);
            byte[] plainText = decipher.doFinal(message);
            return StringUtils.newStringUtf8(plainText);
        } catch (Exception e) {
            log.error("[aes解密失败] key:{} msg:{} eName:{} eMsg:{}", sKey, msg, e.getClass().getSimpleName(), e.getMessage());
            throw new CipherException(ErrorCode.cipher.CIPHER_INSTANCE);
        }

    }

    private static byte[] getKeyBytes(String sKey) throws Exception {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digestOfPassword = md.digest(sKey.getBytes("UTF-8"));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        int j = 0;
        for (int k = 16; j < 8; ) {
            keyBytes[(k++)] = keyBytes[(j++)];
        }
        return keyBytes;
    }
}
