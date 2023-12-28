package com.czhao.test.jdk21;

import javax.crypto.KEM;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author zhaochun
 */
public class TestKEM {
    public static void main(String[] args) throws Exception {
        var me = new TestKEM();
        me.test01();
        me.test02();
        me.test03();
    }

    private void test01() {
        try {
            // Receiver side
            // 接收方选择椭圆曲线X25519来生成自己的公私钥
            var kpg = KeyPairGenerator.getInstance("X25519");
            var kp = kpg.generateKeyPair();
            // 公钥
            var publicKey = kp.getPublic();
            var publicKeyBase64 = encodeToBase64(publicKey);
            System.out.println("pubKeyBase64 = " + publicKeyBase64);
            System.out.println("priKeyBase64 = " + encodeToBase64(kp.getPrivate()));

            // Receiver公开自己的公钥 publicKeyBase64 发送给 Sender

            // Sender side
            // 选择KEM算法，目前只有 DHKEM 可选:
            // https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html
            var kem1 = KEM.getInstance("DHKEM");
            // 使用接收方公开的公钥来生成 密钥封装器
            var sender = kem1.newEncapsulator(decodePublicKeyFromBase64(publicKeyBase64));
            // 生成密钥并封装为密钥消息
            var encapsulated = sender.encapsulate();
            // 对 encapsulated.encapsulation() 做base64编码
            var encapsulatedBase64 = Base64.getEncoder().encodeToString(encapsulated.encapsulation());
            var k1 = encapsulated.key();
            System.out.println("发送密钥1:" + Base64.getEncoder().encodeToString(k1.getEncoded()));

            // 模拟 Sender 将 encapsulatedBase64 发送给 Receiver

            // Receiver side
            // 接收到 encapsulatedBase64 将其解码
            byte[] encapsulatedInReceiver = Base64.getDecoder().decode(encapsulatedBase64);
            var kem2 = KEM.getInstance("DHKEM");
            var receiver = kem2.newDecapsulator(kp.getPrivate());
            var k2 = receiver.decapsulate(encapsulatedInReceiver);
            System.out.println("接收密钥1:" + Base64.getEncoder().encodeToString(k2.getEncoded()));

            assert Arrays.equals(k1.getEncoded(), k2.getEncoded());

            // Sender side
            // 再次生成密钥并发送
            var encapsulated2 = sender.encapsulate();
            var encapsulatedBase642 = Base64.getEncoder().encodeToString(encapsulated2.encapsulation());
            var k3 = encapsulated2.key();
            System.out.println("发送密钥2:" + Base64.getEncoder().encodeToString(encapsulated2.key().getEncoded()));

            // Receiver side
            byte[] encapsulatedInReceiver2 = Base64.getDecoder().decode(encapsulatedBase642);
            var k4 = receiver.decapsulate(encapsulatedInReceiver2);
            System.out.println("接收密钥2:" + Base64.getEncoder().encodeToString(k4.getEncoded()));

            // 比较 k3 k4
            assert Arrays.equals(k3.getEncoded(), k4.getEncoded());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private void test02() throws Exception {
        // 生成公私钥对
        KeyPair keyPair = generateKeyPair();

        // 获取公钥
        PublicKey publicKey = keyPair.getPublic();

        // 获取私钥
        PrivateKey privateKey = keyPair.getPrivate();

        // 将公钥进行Base64编码得到字符串
        String publicKeyBase64 = encodeToBase64(publicKey);

        System.out.println("Encoded Public Key: " + publicKeyBase64);

        // 将Base64编码的字符串解码为公钥
        PublicKey decodedPublicKey = decodePublicKeyFromBase64(publicKeyBase64);

        // 验证原始公钥和解码后的公钥是否相同
        System.out.println("Original Public Key equals Decoded Public Key: " + publicKey.equals(decodedPublicKey));

        // 将私钥进行Base64编码得到字符串
        String privateKeyBase64 = encodeToBase64(privateKey);

        System.out.println("Encoded Private Key: " + privateKeyBase64);

        // 将Base64编码的字符串解码为私钥
        PrivateKey decodedPrivateKey = decodePrivateKeyFromBase64(privateKeyBase64);

        // 验证原始私钥和解码后的私钥是否相同
        System.out.println("Original Private Key equals Decoded Private Key: " + privateKey.equals(decodedPrivateKey));
    }

    private void test03() {
        for (Provider provider : Security.getProviders()) {
            for (Provider.Service service : provider.getServices()) {
//                System.out.println(service.getAlgorithm());
                var algorithm = service.getAlgorithm();
                if (algorithm.endsWith("KEM")) {
                    System.out.println(algorithm);
                }
            }
        }
    }


    // 生成公私钥对
    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("X25519");
        return keyPairGenerator.generateKeyPair();
    }

    // 将公钥或私钥进行Base64编码得到字符串
    private static String encodeToBase64(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // 将Base64编码的字符串解码为公钥
    private static PublicKey decodePublicKeyFromBase64(String base64String) throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(base64String);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("X25519");
        return keyFactory.generatePublic(keySpec);
    }

    // 将Base64编码的字符串解码为私钥
    private static PrivateKey decodePrivateKeyFromBase64(String base64String) throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(base64String);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("X25519");
        return keyFactory.generatePrivate(keySpec);
    }
}
