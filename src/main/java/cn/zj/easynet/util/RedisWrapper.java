package cn.zj.easynet.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;




import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SortingParams;


public class RedisWrapper {
	private static JedisPool jedisPool = null;  //非切片连接池
	private volatile static RedisWrapper REDISUTILS = null;
	private JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
	
	private RedisWrapper() {
		initialPool();
	}
	
	/**
	 * 
	 */
	public static RedisWrapper getInstance() {
		if (REDISUTILS == null) {
			synchronized (RedisWrapper.class) {
				if (REDISUTILS == null) {
					REDISUTILS = new RedisWrapper();
				}
			}
		}
		return REDISUTILS;
	}
	
	 /**
     * 
     */
    private void initialPool() 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxTotal(200);
        config.setMaxIdle(50);
        config.setMaxWaitMillis(1000 * 4);
        
        jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPoolConfig(config);
        jedisConnectionFactory.setHostName(ConfigUtil.REDIS_LIST);
        jedisConnectionFactory.setPort(Integer.parseInt( ConfigUtil.REDIS_PORT) );
        jedisConnectionFactory.setPassword("");
        jedisConnectionFactory.afterPropertiesSet();
    }
    
    public JedisConnection getConnect(){
    	return jedisConnectionFactory.getConnection();
    }
	
    public void hDel(byte[] key, byte[] field){
    	JedisConnection conn = getConnect();
    	conn.hDel(key, field);
    	conn.close();
    }
    
    public String hGet(byte[] key, byte[] field){
    	JedisConnection conn = getConnect();
    	String retStr = new String( conn.hGet(key, field) );
    	conn.close();
    	return retStr;
    }
    
    public void hSet(byte[] key, byte[] field, byte[] value){
    	JedisConnection conn = getConnect();
    	conn.hSet(key, field, value);
    	conn.close();
    }
	
}
