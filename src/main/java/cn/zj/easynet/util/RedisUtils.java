package cn.zj.easynet.util;

import redis.clients.jedis.Jedis;

/***
 * jedis简易工具类
 * @author roy
 *
 */
public class RedisUtils {
	public static Long expireKey(String redisServerUrl , int port, String key, int seconds){
		Jedis jedis = new Jedis(redisServerUrl , port);
		Long k = jedis.expire(key, seconds);
		jedis.close();
		return k;
	}
	
	public static boolean exisitKey(String redisServerUrl ,int port, String key){
		Jedis jedis = new Jedis(redisServerUrl , port);
		boolean resp = jedis.exists(key);
		jedis.close();
		return resp;
	}
	
	public static void setKey(String redisServerUrl ,int port, String key,String value){
		Jedis jedis = new Jedis(redisServerUrl , port);
		jedis.set(key, value);
		jedis.close();
		
	}
	
	public static Long setAndExpireKey(String redisServerUrl ,int port, String key,String value, int seconds){
		Jedis jedis = new Jedis(redisServerUrl , port);
		jedis.set(key, value);
		Long k = jedis.expire(key, seconds);
		jedis.close();
		return k;
	}
	
	public static String getKey(String redisServerUrl ,int port, String key){
		Jedis jedis = new Jedis(redisServerUrl , port);
		String resp = jedis.get(key);
		jedis.close();
		return resp;
	}

}
