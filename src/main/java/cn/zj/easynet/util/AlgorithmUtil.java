package cn.zj.easynet.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AlgorithmUtil {
	public static String md5(String src) {
		if (src == null)
			throw new IllegalArgumentException("Password can NOT be null");
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(src.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {}
		return null;
	}
	
	public static String md5_1(String src) {
		if (src == null)
			throw new IllegalArgumentException("Password can NOT be null");
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(src.getBytes());
			
			return new String(array, "utf-8");
			
		} catch (Exception e) {}
		return null;
	}
	
	public static String md5_2(String src) {
        if (src == null)
            throw new IllegalArgumentException("Password can NOT be null");
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(src.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString(array[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {}
        return null;
    }
	
	public static void main(String[] args) {
		System.out.println(Integer.toBinaryString(-5));
		String md1 = md5("123");
	    String md2 = md5_1("123");
		System.out.println(String.format("md1: %s ,md1.length(): %s", md1, md1.length()));
		System.out.println(String.format("md2: %s ,md2.length(): %s", md2, md2.length()));
		
	}
}