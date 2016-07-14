/*
 * cn.zj.easynet.yx.platfrom.RedisTest.java
 * Aug 21, 2014 
 */
package cn.zj.easynet;

import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import cn.zj.easynet.device.http.HttpClientPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Aug 21, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class RedisTest {
	private static final Logger logger = Logger.getLogger(RedisTest.class);

     private static final String KEY_USERID_PERFIX = "YHT_USERID_";
     private static final String KEY_NUMBER_PERFIX = "YHT_NUMBER_";

    private static final String queryBusiNbr_uid_ = "queryBusiNbr_uid_";
    private static final String queryUid_busiNbr_ = "queryUid_busiNbr_";

    private static final String queryEcpid_uid_ = "queryEcpid_uid_";
    private static final String queryUid_ecpid_ = "queryUid_ecpid_";

    
    private static final String host = "192.168.21.2";
    private static final int port = 9999;
    private static final String key = "YHT_NUMBER_057181932782";
    /**
     * @param args
     */
    public static void main(String[] args) {
//        testSingle();
    	watchTest();
    }
    
    public static void watchTest(){
    	
    	 JedisPoolConfig pc = new JedisPoolConfig();
         pc.setMaxTotal(20);
         pc.setMaxIdle(8);
         pc.setMaxWaitMillis(1000 * 2);
         pc.setTestOnBorrow(false);
         
         JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
         jedisConnectionFactory.setPoolConfig(pc);
         jedisConnectionFactory.setHostName(host);
         jedisConnectionFactory.setPort(port);
         jedisConnectionFactory.setPassword("");
         jedisConnectionFactory.setTimeout(10000);
         jedisConnectionFactory.afterPropertiesSet();
         
         
         
    	org.apache.commons.lang3.time.StopWatch watch = new org.apache.commons.lang3.time.StopWatch();
		watch.start();
		for (int i = 1; i <= 1; i++) {
			watch.split();
			testPoll(jedisConnectionFactory);
			
			logger.error(i+ ":\t" +  watch.getTime()/1000d);
		}
    }
    
    public static void testSingle() {
        Jedis redis = new Jedis(host, port);
        String v = redis.get(key);
        redis.close();
        System.out.println("single: "+v);
    }
    
    public static void testPoll(JedisConnectionFactory jedisConnectionFactory) {
    	JedisConnection conn =  jedisConnectionFactory.getConnection();
        System.out.println("poll-conn: "+new String(conn.get(key.getBytes())));
//        conn.close();
        
        
        /*final RedisTemplate<Serializable, Serializable> redis = new RedisTemplate<Serializable, Serializable>();
        redis.setConnectionFactory(jedisConnectionFactory);
        redis.afterPropertiesSet();
        String v = redis.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection conn) throws DataAccessException {
                byte[] redisKey = redis.getStringSerializer().serialize(key);
                if (conn.exists(redisKey)) {
                    byte[] key = conn.get(redisKey);
                    String value = redis.getStringSerializer().deserialize(key);
                    return value;
                }
                return null;
            }
        });
        
        System.out.println("poll-template: "+v);*/
    }

    public static void dealBusiNbr(Jedis redis) {
        Set<String> keys = redis.keys(queryBusiNbr_uid_ + "*");
        for (String key : keys) {
            redis.del(key);
//            String mobile = redis.get(key);
//            String uid = key.replace(queryBusiNbr_uid_, "");
//
//            String nKey = (queryEcpid_uid_ + uid).trim();
//            String nValue = (mobile).trim();
//            if (!redis.exists(nKey)) {
//                 redis.set(nKey, nValue);
//                System.out.println(nKey + ":" + nValue);
//            }

        }

        keys = redis.keys(queryUid_busiNbr_ + "*");
        for (String key : keys) {
            redis.del(key);
//            String uid = redis.get(key);
//            String mobile = key.replace(queryUid_busiNbr_, "");
//
//            String nKey = (queryUid_ecpid_ + mobile).trim();
//            String nValue = (uid).trim();
//            if (!redis.exists(nKey)) {
//                 redis.set(nKey, nValue);
//                System.out.println(nKey + ":" + nValue);
//            }
        }
    }

    public static void dealYHT(Jedis redis) {
        Set<String> keys = redis.keys("YHT_*");
        for (String key : keys) {
            if (key.startsWith(KEY_USERID_PERFIX)) {
                String mobile = redis.get(key);
                String uid = key.replace(KEY_USERID_PERFIX, "");

                String nKey = (queryEcpid_uid_ + uid).trim();
                String nValue = mobile.trim();
                if (!redis.exists(nKey)) {
                     redis.set(nKey, nValue);
                    System.out.println(nKey + ":" + nValue);
                }

            } else if (key.startsWith(KEY_NUMBER_PERFIX)) {
                String uid = redis.get(key);
                String mobile = key.replace(KEY_NUMBER_PERFIX, "");

                String nKey = (queryUid_ecpid_ + mobile).trim();
                String nValue = uid.trim();
                if (!redis.exists(nKey)) {
                     redis.set(nKey, nValue);
                    System.out.println(nKey + ":" + nValue);
                }
            }
        }
    }

}
