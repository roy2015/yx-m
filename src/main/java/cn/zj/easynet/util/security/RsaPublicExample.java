package cn.zj.easynet.util.security;

/**
 * PublicExample.java Copyright 2005-2-16
 */
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

/**
 * 一个简单的公鈅加密例子,Cipher类使用KeyPairGenerator生成的公鈅和私鈅
 */
public class RsaPublicExample {

    public static void test(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage:java PublicExample ");
            System.exit(1);
        }

        byte[] plainText = args[0].getBytes("UTF8");
        // 构成一个RSA密钥
        System.out.println(" Start generating RSA key");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();
        System.out.println("Finish generating RSA key");

        // 获得一 个RSA的Cipher类，使用公鈅加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        System.out.println(" " + cipher.getProvider().getInfo());

        System.out.println(" Start encryption");
        cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
        byte[] cipherText = cipher.doFinal(plainText);
        System.out.println("Finish encryption:");
        System.out.println(new String(cipherText, "UTF8"));

        // 使用私鈅解密
        System.out.println(" Start decryption");
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
        byte[] newPlainText = cipher.doFinal(cipherText);
        System.out.println("Finish decryption:");
        System.out.println(new String(newPlainText, "UTF8"));
    }
}
