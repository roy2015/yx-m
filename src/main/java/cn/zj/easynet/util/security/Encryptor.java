package cn.zj.easynet.util.security;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.zj.easynet.util.MainTest;

import com.sun.crypto.provider.SunJCE;

public class Encryptor
  implements IEncryptor
{
  public static final String ENC_DES = "DES";
  public static final String ENC_DESede = "DESede";
  public static final String ENC_Blowfish = "Blowfish";
  private static final Logger log = Logger.getLogger(MainTest.class);
  
  private String encoding;
  private String algorithm;
  private Key key;

  public Encryptor(String keySeed)
  {
    this(null, keySeed, null);
  }

  public Encryptor(String algorithm, String keySeed) {
    this(algorithm, keySeed, null);
  }

  public Encryptor(String algorithm, String keySeed, String encoding)
  {
    setEncoding(encoding);
    setAlgorithm(algorithm);
    initKey(getAlgorithm(), keySeed);
  }

  private void initKey(String algorithm, String keySeed)
  {
    Security.addProvider(new SunJCE());
    try {
      KeyGenerator kg = KeyGenerator.getInstance(algorithm);
      SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
      sr.setSeed(keySeed.getBytes(getEncoding()));
      kg.init(sr);
      this.key = kg.generateKey();
      kg = null;
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
  }

  public String getEncoding()
  {
    return this.encoding;
  }

  public void setEncoding(String encoding)
  {
    this.encoding = (StringUtils.isBlank(encoding) ? "utf-8" : encoding);
  }

  public String getAlgorithm()
  {
    return this.algorithm;
  }

  public void setAlgorithm(String algorithm)
  {
    this.algorithm = (StringUtils.isBlank(algorithm) ? "DESede" : algorithm);
  }

  public String encrypt(String src)
  {
    try
    {
      byte[] byteMi = getDesCoder(1, src.getBytes(getEncoding()));
      return new String(EncodeUtils.hexEncode(byteMi));
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    return null;
  }

  public String decrypt(String src)
  {
    try
    {
      byte[] byteMing = getDesCoder(2, EncodeUtils.hexDecode(src));
      return new String(byteMing, getEncoding());
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    return null;
  }

  public String encryptAndBase64(String src)
  {
    try
    {
      byte[] byteMi = getDesCoder(1, src.getBytes(getEncoding()));
      return new String(EncodeUtils.base64Encode(byteMi));
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    return null;
  }

  public String decryptAndBase64(String src)
  {
    try
    {
      byte[] byteMing = getDesCoder(2, EncodeUtils.base64Decode(src));
      return new String(byteMing, getEncoding());
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    return null;
  }

  private byte[] getDesCoder(int mode, byte[] byteS) {
    try {
      Cipher cipher = Cipher.getInstance(getAlgorithm());
      cipher.init(mode, this.key);
      return cipher.doFinal(byteS);
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    return null;
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    String key = "EC415B9AES5E2W2FJ4TB46DAUKFD8H45";
    Encryptor des = new Encryptor("DESede", key, "utf-8");

    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String passwordStr = "uid"+"3732001" + "sno" + format.format(new Date());
    System.out.println("加密前的字符串:" + passwordStr);

    String strEnc = des.encryptAndBase64(passwordStr);
    System.out.println("加密后Base64的字符串:" + strEnc);

    String urlEncoderStr = URLEncoder.encode(strEnc,"utf-8");
    System.out.println("urlencode加密后Base64的字符串:" + urlEncoderStr);

    String urldecoderStr = URLDecoder.decode(urlEncoderStr,"utf-8");
    System.out.println("urldncode加密后Base64的字符串:" + urldecoderStr);
    
    String strDes = des.decryptAndBase64(urldecoderStr);
    
    if(strDes == null){
    	System.out.println("解密失败!");
    }else{
    	System.out.println("Base64解密后的字符串:" + strDes);
    }
    
    
   }
}