package cn.zj.easynet.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lei
 * @since 2012-12-14
 */
public class EncryptUtil {
	private static final Logger log = LoggerFactory
			.getLogger(EncryptUtil.class);
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String fKey = "NJ3r05t949R9jdkdfo4lDLR2Evzl35Rkdl1tggtjofdKRIOkLH888iJkyUkjNNbVvjU84410Keloekri78DJ490I574RjK96HjJt7676554r5tgjhHhBGY78668754631HIUHUggGgyGFY78684Ffhyj6JJBN464335dfDDXZccblpoppytrdrdfGFtrgjii87pdl545";

	
	public static String encryptFull(String srcStr) {
		return encryptFull(srcStr, DEFAULT_CHARSET);
	}

	public static String encryptFull(String srcStr, String charset) {
		StringBuilder builder = new StringBuilder();
		try {
			byte[] srcByte = srcStr.getBytes(charset);
			byte[] keyByte = fKey.getBytes(charset);

			// 异或
			for (int i = 0; i < srcByte.length; i++) {
				srcByte[i] = (byte) (srcByte[i] ^ keyByte[i % fKey.length()]);
			}

			// 编码成16进制数组
			for (byte item : srcByte) {
				String hex = Integer.toHexString(item & 0xFF);
				if (hex.length() == 1)
					hex = "0" + hex;
				builder.append(hex);
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}

		return builder.toString();
	}

	public static String decryptFull(String encryptStr) {
		return decryptFull(encryptStr, DEFAULT_CHARSET);
	}

	public static String decryptFull(String encryptStr, String charset) {
		String srcStr = null;
		try {
			int length = encryptStr.length();
			byte[] encryptByte = new byte[length / 2];

			// 16进制数组转换会byte数组
			for (int i = 0; i < length / 2; ++i) {
				encryptByte[i] = (byte) Integer.parseInt(
						encryptStr.substring(i * 2, i * 2 + 2), 16);
			}

			byte[] keyByte = fKey.getBytes(charset);// 密钥转换成字节数组

			// 异或
			for (int i = 0; i < encryptByte.length; ++i) {
				encryptByte[i] = (byte) (encryptByte[i] ^ keyByte[i
						% keyByte.length]);
			}

			// byte数组还原成字符串
			srcStr = new String(encryptByte, charset);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}

		return srcStr;
	}

	public static String MD(String algorithm, String value) {
		if (value == null)
			return null;

		String result = null;
		try {
			MessageDigest alga = MessageDigest.getInstance(algorithm);
			alga.update(value.getBytes());
			byte[] digest = alga.digest();
			result = new BigInteger(1, digest).toString(16);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.toString(), e);
		}
		return result;
	}

	public static String MD5(String value) {
		return MD("MD5", value);
	}

	public static String SHA1(String value) {
		return MD("SHA-1", value);
	}
	
	public static void main(String[] args) {
		System.out.println( decryptFull("35685e0157761b5d511b680c5a5647460b1c533f303e3b5c2254404e755a204b0d0241011347071e1d0f0a2c686913493735641a1a4568181c271d0f3c1a0b3b13486f09000100097353585f5d5e45504a") );
	}

}
