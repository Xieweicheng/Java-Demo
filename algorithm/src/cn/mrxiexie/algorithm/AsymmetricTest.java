package cn.mrxiexie.algorithm;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * 非对称加密
 *
 * @author mrxiexie
 * @date 19-9-26 下午11:02
 */
public class AsymmetricTest {

    private static final String ORIGINAL = "123456";

    /**
     * RSA/ECB/PKCS1Padding
     */
    @Test
    public void RSA_ECB_PKC1Padding() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = gen.generateKeyPair();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // 加密模式
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        byte[] bytes = cipher.doFinal(ORIGINAL.getBytes());
        // 解密模式
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
        System.out.println("私钥长度 : " + keyPair.getPrivate().getEncoded().length * 8);
        System.out.println("公钥长度 : " + keyPair.getPublic().getEncoded().length * 8);
        System.out.println("密文 : " + HexUtil.encodeHexStr(bytes));
        System.out.println("原文 : " + new String(cipher.doFinal(bytes)));
    }

    /**
     * RSA/ECB/PKCS1Padding publicKeyEncoded 转为 publicKey 示例
     */
    @Test
    public void RSA_ECB_PKCS1Padding_Public_Key() throws Exception {
        int keySize = 512;
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair keyPair = keyGen.generateKeyPair();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // 加密模式
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        cipher.update(ORIGINAL.getBytes());
        byte[] bytes = cipher.doFinal();
        // 获取到公钥的字节数组
        byte[] publicKeyEncoded = keyPair.getPublic().getEncoded();
        // 通过公钥的字节数组获取到公钥对象
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyEncoded));
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // 解密模式
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        cipher.update(bytes);
        System.out.println("私钥长度 : " + keyPair.getPrivate().getEncoded().length * 8);
        System.out.println("公钥长度 : " + keyPair.getPublic().getEncoded().length * 8);
        System.out.println("密文 : " + HexUtil.encodeHexStr(bytes));
        System.out.println("原文 : " + new String(cipher.doFinal()));
    }

    /**
     * RSA/ECB/PKCS1Padding privateKeyEncoded 转为 privateKey 示例
     */
    @Test
    public void RSA_ECB_PKCS1Padding_Private_Key() throws Exception {
        int keySize = 512;
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair keyPair = keyGen.generateKeyPair();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // 加密模式
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        cipher.update(ORIGINAL.getBytes());
        byte[] bytes = cipher.doFinal();
        // 获取到私钥的字节数组
        byte[] privateKeyEncoded = keyPair.getPrivate().getEncoded();
        // 通过私钥的字节数组获取到私钥对象
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyEncoded));
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // 解密模式
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        cipher.update(bytes);
        System.out.println("私钥长度 : " + keyPair.getPrivate().getEncoded().length * 8);
        System.out.println("公钥长度 : " + keyPair.getPublic().getEncoded().length * 8);
        System.out.println("密文 : " + HexUtil.encodeHexStr(bytes));
        System.out.println("原文 : " + new String(cipher.doFinal()));
    }

    /**
     * 使用 RSA 的 {@link Cipher#ENCRYPT_MODE} 加密 SecretKey 得到 result1，
     * 然后将 result1 用 {@link Cipher#UNWRAP_MODE} 解密重新获取 SecretKey
     * <p>
     * 使用 RSA 的 {@link Cipher#WRAP_MODE} 加密 SecretKey 得到 result2，
     * 然后将 result2 用 {@link Cipher#DECRYPT_MODE} 解密重新获取 SecretKey
     * <p>
     * 两次结果一致，说明两种方式互通，只是一个返回 byte[] 一个返回 key
     */
    @Test
    public void wrapMode() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        SecretKey sessionKey = new SecretKeySpec(new byte[16], "AES");

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] result1 = cipher.doFinal(sessionKey.getEncoded());

        cipher.init(Cipher.WRAP_MODE, keyPair.getPublic());
        byte[] result2 = cipher.wrap(sessionKey);

        cipher.init(Cipher.UNWRAP_MODE, keyPair.getPrivate());
        SecretKey sessionKey1 = (SecretKey) cipher.unwrap(result1, "AES",
                Cipher.SECRET_KEY);

        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        SecretKey sessionKey2 = new SecretKeySpec(cipher.doFinal(result2), "AES");

        System.out.println(Arrays.equals(sessionKey1.getEncoded(),
                sessionKey2.getEncoded()));
    }
}
