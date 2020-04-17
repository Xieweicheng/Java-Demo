package cn.mrxiexie.algorithm;

import org.junit.Test;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * @author mrxiexie
 * @date 19-9-26 下午11:15
 */
public class SignatureTest {

    private static final String KEY_ALGORITHM = "RSA";

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private static final String PUBLIC_KEY = "PUBLIC_KEY";
    private static final String PRIVATE_KEY = "PRIVATE_KEY";

    private static final String ORIGINAL = "123456";

    /**
     * 签名
     * <p>
     * 只有私钥/公钥对的所有者才能创建签名。对于只有公钥和许多签名来恢复私钥的人来说，计算上是不可行的。
     * 给定与用于生成签名的私钥相对应的公钥，应该可以验证输入的真实性和完整性。
     */
    @Test
    public void signature() throws Exception {
        Map<String, String> map = initKey();
        byte[] publicKey = getPublicKey(map);
        byte[] privateKey = getPrivateKey(map);
        byte[] cipherStr = encrypt(ORIGINAL.getBytes(), privateKey);
        System.out.println("原文：" + new String(decrypt(cipherStr, publicKey)));
        byte[] sign = sign(ORIGINAL.getBytes(), privateKey);
        System.out.println("校验：" + verify(ORIGINAL.getBytes(), sign, publicKey));
    }

    /**
     * 初始化 DSA 并把公私钥 base64 后存储在 map 中
     */
    private Map<String, String> initKey() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        KeyPair keyPair = gen.generateKeyPair();
        // 甲方公钥
        PublicKey publicKey = keyPair.getPublic();
        // 甲方私钥
        PrivateKey privateKey = keyPair.getPrivate();
        Map<String, String> keyMap = new HashMap<>(4);
        keyMap.put(PUBLIC_KEY, Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        keyMap.put(PRIVATE_KEY, Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        return keyMap;
    }

    /**
     * 签名
     */
    private byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成私钥
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 实例化Signature
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        // 初始化Signature
        signature.initSign(priKey);
        // 更新
        signature.update(data);
        return signature.sign();
    }

    /**
     * 校验
     */
    private boolean verify(byte[] data, byte[] sign, byte[] publicKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        // 实例化Signature
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        // 初始化Signature
        signature.initVerify(pubKey);
        // 更新
        signature.update(data);
        // 验证
        return signature.verify(sign);
    }

    /**
     * 加密
     */
    private byte[] encrypt(byte[] data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成私钥
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // 加密模式
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     */
    private byte[] decrypt(byte[] cipherStr, byte[] publicKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // 解密模式
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(cipherStr);
    }

    /**
     * 获取私钥 byte 数组
     */
    private byte[] getPrivateKey(Map<String, String> keyMap) {
        return Base64.getDecoder().decode(keyMap.get(PRIVATE_KEY));
    }

    /**
     * 获取公钥 byte 数组
     */
    private byte[] getPublicKey(Map<String, String> keyMap) {
        return Base64.getDecoder().decode(keyMap.get(PUBLIC_KEY));
    }
}
