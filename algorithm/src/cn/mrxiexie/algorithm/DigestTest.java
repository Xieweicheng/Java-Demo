package cn.mrxiexie.algorithm;

import org.junit.After;
import org.junit.Test;

import java.security.MessageDigest;

/**
 * 摘要算法
 *
 * @author mrxiexie
 * @date 19-9-26 下午2:27
 */
public class DigestTest {

    private static final String ORIGINAL = "123456";

    private byte[] cipher;

    @After
    public void onAfter() {
        System.out.println(String.format("摘要长度：%d", cipher.length));
        System.out.println(HexUtil.encodeHexStr(cipher));
    }

    /**
     * md5 为 128 位
     */
    @Test
    public void md5() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        cipher = md.digest(ORIGINAL.getBytes());
    }

    /**
     * sha1 为 160 位
     */
    @Test
    public void sha1() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        cipher = md.digest(ORIGINAL.getBytes());
    }

    /**
     * sha256 为 256 位
     */
    @Test
    public void sha256() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        cipher = md.digest(ORIGINAL.getBytes());
    }

    /**
     * sha256 为 512 位
     */
    @Test
    public void sha512() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        cipher = md.digest(ORIGINAL.getBytes());
    }
}
