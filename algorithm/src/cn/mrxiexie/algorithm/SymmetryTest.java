package cn.mrxiexie.algorithm;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * 对称加密
 *
 * @author mrxiexie
 * @date 19-9-26 下午3:17
 */
public class SymmetryTest {

    private static final String SEED = "SEED";

    private static final String ORIGINAL = "123456";

    private static final String ORIGINAL16 = "1234567890123456";

    private static final byte[] KEY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    /**
     * AES/ECB/PKCS5Padding
     * <p>
     * 密钥 AES 128
     * 填充 PKCS5Padding
     * 模式 ECB
     */
    @Test
    public void AES_ECB_PKCS5Padding() throws Exception {
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(128, new SecureRandom(SEED.getBytes()));
        SecretKey secretKey = gen.generateKey();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 加密模式
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        cipher.update(ORIGINAL.getBytes());
        byte[] bytes = cipher.doFinal();
        // 解密模式
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        cipher.update(bytes);
        System.out.println("密钥长度 : " + secretKey.getEncoded().length * 8);
        System.out.println("密文 : " + HexUtil.encodeHexStr(bytes));
        System.out.println("原文 : " + new String(cipher.doFinal()));
    }

    /**
     * AES/ECB/NoPadding
     * <p>
     * 密钥 AES 128
     * 填充 NoPadding 原文长度必须是 16 的倍数
     * 模式 ECB
     */
    @Test
    public void AES_ECB_NoPadding() throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        // 加密模式
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] bytes = cipher.doFinal(ORIGINAL16.getBytes());
        // 解密模式
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        System.out.println("密钥长度 : " + secretKeySpec.getEncoded().length * 8);
        System.out.println("密文 : " + HexUtil.encodeHexStr(bytes));
        System.out.println("原文 : " + new String(cipher.doFinal(bytes)));
    }

    /**
     * AES/CBC/ISO10126Padding
     * <p>
     * 密钥 AES 256
     * 填充 ISO10126Padding
     * 模式 CBC
     * 初始向量 IV 长度必须为 16
     */
    @Test
    public void AES_CBC_ISO10126Padding() throws Exception {
        String ivStr = "1234567890123456";
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(256, new SecureRandom(SEED.getBytes()));
        SecretKey secretKey = gen.generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
        // 加密模式
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] bytes = cipher.doFinal(ORIGINAL.getBytes());
        // 解密模式
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        cipher.doFinal(bytes);
        System.out.println("密钥长度 : " + secretKey.getEncoded().length * 8);
        System.out.println("密文 : " + HexUtil.encodeHexStr(bytes));
        System.out.println("原文 : " + new String(cipher.doFinal(bytes)));
    }
}
