package cn.zj.easynet.util.security;

public abstract interface ISimpleEncryptor
{
  public abstract String encrypt(String paramString);

  public abstract String decrypt(String paramString);
}