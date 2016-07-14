package cn.zj.easynet.util.security;

public abstract interface IEncryptor extends ISimpleEncryptor
{
  public abstract String encryptAndBase64(String paramString);

  public abstract String decryptAndBase64(String paramString);
}