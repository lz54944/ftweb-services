package com.hhwy.auth.core.util;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.util.StringUtils;


/**
 * @Descriptions: $
 * @Author: LKHang
 * @Date: 2022/9/16$ 14:58$
 */
public class RsaUtils {

    //这个密钥需要是16位
    public static final String KEY_DES = "8f4a6691cfef42fe";

    public static void main(String[] args) throws Exception {
        String old = "admin123.";

        //String jia = RsaUtils.aesEncryptForFront(old,RsaUtils.KEY_DES);

        String jia = "3c9f0aeef1a4628ba0f23778d09fcab3";
        //加密
        System.out.println("加密后："+jia);

        String jie = RsaUtils
            .aesDecryptForFront(jia, RsaUtils.KEY_DES);
        //解密
        System.out.println("解密后"+jie);


    }


    /**
     * AES解密
     * @param encryptStr 密文
     * @param decryptKey 秘钥，必须为16个字符组成
     * @return 明文
     * @throws Exception
     */
    public static String aesDecryptForFront(String encryptStr, String decryptKey) {
        if (StringUtils.isEmpty(encryptStr) || StringUtils.isEmpty(decryptKey)) {
            return null;
        }
        try {
            byte[] encryptByte = Base64.getDecoder().decode(encryptStr);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
            byte[] decryptBytes = cipher.doFinal(encryptByte);
            return new String(decryptBytes);

        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }


    }

    /**
     * AES加密
     * @param content 明文
     * @param encryptKey 秘钥，必须为16个字符组成
     * @return 密文
     * @throws Exception
     */
    public static String aesEncryptForFront(String content, String encryptKey) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(encryptKey)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

            byte[] encryptStr = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptStr);

        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }

    }

}
