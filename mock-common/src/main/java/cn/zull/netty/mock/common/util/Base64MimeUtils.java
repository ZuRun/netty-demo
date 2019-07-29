package cn.zull.netty.mock.common.util;


import org.apache.tomcat.util.codec.binary.StringUtils;

import java.util.Base64;

/**
 * @author zurun
 * @date 2018/5/10 10:56:27
 */
public class Base64MimeUtils {
    /**
     * BASE64解密
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(byte[] encryptedData) {
        return Base64.getMimeDecoder().decode(encryptedData);
    }

    public static byte[] decryptBASE64(String encryptedData) {
        return decryptBASE64(StringUtils.getBytesUtf8(encryptedData));
    }

    public static String decryptToString(String encryptedData) {
        return decryptToString(StringUtils.getBytesUtf8(encryptedData));
    }

    public static String decryptToString(byte[] encryptedData) {
        return StringUtils.newStringUtf8(decryptBASE64(encryptedData));
    }

    /**
     * BASE64加密
     *
     * @param decryptedData
     * @return
     * @throws Exception
     */
    public static byte[] encryptBASE64(byte[] decryptedData) {
        return Base64.getMimeEncoder().encode(decryptedData);
    }

    public static byte[] encryptBASE64(String decryptedData) {
        return encryptBASE64(StringUtils.getBytesUtf8(decryptedData));
    }

    public static String encryptToString(String decryptedData) {
        return encryptToString(StringUtils.getBytesUtf8(decryptedData));
    }

    public static String encryptToString(byte[] decryptedData) {
        return StringUtils.newStringUtf8(encryptBASE64(decryptedData));
    }


    public static void main(String[] args) {
        String data = "http://aub.iteye.com/";
        System.out.println("加密前：" + data);
        System.out.println("加密后：" + encryptBASE64(data));
        System.out.println("加密后：" + encryptToString(data));
        System.out.println("解密后：" + decryptToString(encryptToString(data)));
        System.out.println("解密后：" + decryptToString(encryptBASE64(data)));
    }
}
