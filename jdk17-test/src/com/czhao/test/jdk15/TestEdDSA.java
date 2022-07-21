package com.czhao.test.jdk15;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * JEP 339: Edwards-Curve Digital Signature Algorithm (EdDSA)
 *
 * @author zhaochun
 */
public class TestEdDSA {
    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        TestEdDSA me = new TestEdDSA();
        me.testEdDSA("测试 Ed25519 签名");
        me.testEdDSA("");
        me.testECDSA("测试 ECDSA 签名");
        me.testECDSA("");
    }

    private void testEdDSA(String msg) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // 获取明文字节数组
        byte[] plaintext = msg.getBytes(StandardCharsets.UTF_8);
        // 根据椭圆曲线 Ed25519 生成公私钥
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519");
        KeyPair kp = kpg.generateKeyPair();
        // 指定签名算法 Ed25519
        Signature sig = Signature.getInstance("Ed25519");
        // 传入私钥
        sig.initSign(kp.getPrivate());
        // 传入明文
        sig.update(plaintext);
        // 签名
        byte[] s = sig.sign();
        // 对签名结果做Base64编码
        String signStr = Base64.getEncoder().encodeToString(s);
        System.out.println(signStr);

        // 指定签名算法 Ed25519
        Signature verifier = Signature.getInstance("Ed25519");
        // 传入公钥
        verifier.initVerify(kp.getPublic());
        // 传入明文
        verifier.update(plaintext);
        // 验签
        boolean signVerify = verifier.verify(s);
        System.out.println(signVerify);
    }

    private void testECDSA(String msg) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // 明文
        byte[] plaintext = msg.getBytes(StandardCharsets.UTF_8);
        // 使用椭圆曲线 EC 生成公私钥
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        KeyPair kp = kpg.generateKeyPair();
        // 指定签名算法 SHA256withECDSA
        Signature sig = Signature.getInstance("SHA256withECDSA");
        // 传入私钥
        sig.initSign(kp.getPrivate());
        // 传入明文
        sig.update(plaintext);
        // 签名
        byte[] s = sig.sign();
        // 对签名做Base64编码
        String signStr = Base64.getEncoder().encodeToString(s);
        System.out.println(signStr);

        // 指定签名算法 SHA256withECDSA
        Signature verifier = Signature.getInstance("SHA256withECDSA");
        // 传入公钥
        verifier.initVerify(kp.getPublic());
        // 传入明文
        verifier.update(plaintext);
        // 验签
        boolean signVerify = verifier.verify(s);
        System.out.println(signVerify);
    }
}