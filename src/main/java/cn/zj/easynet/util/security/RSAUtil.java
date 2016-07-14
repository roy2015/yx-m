package cn.zj.easynet.util.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import org.apache.log4j.Logger;

public class RSAUtil {

    private Logger              log           = Logger.getLogger(getClass());
    private static RSAUtil      instance;
    private static final String algorithm     = "RSA";
    private static final String signature_alg = "MD5withRSA";
    private static final String charset       = "UTF-8";

    public static RSAUtil getInstance() {
        if (instance == null) return new RSAUtil();
        return instance;
    }

/*    public void generateKeyPair(String key_path, String name_prefix) {
        KeyPairGenerator keygen = null;
        try {
            keygen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e1) {
            this.log.error(e1.getMessage());
        }
        SecureRandom secrand = new SecureRandom();
        Random rd = new Random();
        secrand.setSeed(String.format("%08d", new Object[] { Integer.valueOf(rd.nextInt(99999999)) }).getBytes());
        keygen.initialize(1024, secrand);
        KeyPair keys = keygen.genKeyPair();
        PublicKey pubkey = keys.getPublic();
        PrivateKey prikey = keys.getPrivate();
        String pubKeyStr = com.lianpay.share.utils.Base64.getBASE64(pubkey.getEncoded());
        String priKeyStr = com.lianpay.share.utils.Base64.getBASE64(prikey.getEncoded());
        File file = new File(key_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(key_path + name_prefix + "_priKey.txt"));
            fos.write(priKeyStr.getBytes());
            fos.close();

            fos = new FileOutputStream(new File(key_path + name_prefix + "_pubKey.txt"));
            fos.write(pubKeyStr.getBytes());
        } catch (IOException e) {
            this.log.error(e.getMessage());

            if (fos != null) try {
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (fos != null) try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getKeyContent(String priKeyFile) {
        File file = new File(priKeyFile);
        BufferedReader br = null;
        InputStream ins = null;
        StringBuffer sReturnBuf = new StringBuffer();
        try {
            ins = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            String readStr = null;
            readStr = br.readLine();
            while (readStr != null) {
                sReturnBuf.append(readStr);
                readStr = br.readLine();
            }
        } catch (IOException localIOException1) {
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                    ins = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sReturnBuf.toString();
    }

    public String sign(String priKeyFile, String signSrc) {
        byte[] sign = signature(getKeyContent(priKeyFile), signSrc);
        if (sign == null) {
            return "";
        }
        return com.lianpay.share.utils.Base64.getBASE64(sign);
    }

    public String signed(String priKeyValue, String signSrc) {
        byte[] sign = signature(priKeyValue, signSrc);
        if (sign == null) {
            return "";
        }
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(sign));
    }

    public byte[] signature(String priKeyValue, String signSrc) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                                                                   com.lianpay.share.utils.Base64.getBytesBASE64(priKeyValue));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);

            Signature signet = Signature.getInstance("MD5withRSA");
            signet.initSign(myprikey);
            signet.update(signSrc.getBytes("UTF-8"));
            return signet.sign();
        } catch (Exception e) {
            this.log.error("RSA签名失败," + e.getMessage());
        }
        return null;
    }

    public boolean checksign(String pubKeyFile, String oriStr, String signedStr) {
        return verify(getKeyContent(pubKeyFile), oriStr, signedStr);
    }

    public boolean verify(String pubKeyValue, String oriStr, String signedStr) {
        try {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
                                                                      com.lianpay.share.utils.Base64.getBytesBASE64(pubKeyValue));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            byte[] signed = com.lianpay.share.utils.Base64.getBytesBASE64(signedStr);
            Signature signetcheck = Signature.getInstance("MD5withRSA");
            signetcheck.initVerify(pubKey);
            signetcheck.update(oriStr.getBytes("UTF-8"));
            return signetcheck.verify(signed);
        } catch (Exception e) {
            this.log.error("RSA签名验证异常," + e.getMessage());
        }
        return false;
    }*/

    public static void main(String[] args) {
    }
}
