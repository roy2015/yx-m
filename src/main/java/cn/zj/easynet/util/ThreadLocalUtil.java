package cn.zj.easynet.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by BG244210 on 2016/11/28.
 */
public class ThreadLocalUtil {
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalUtil.class);

    public static ThreadLocal<HashMap<String , Object>> THREAD_LOCAL = new ThreadLocal<HashMap<String , Object>>(){
        @Override
        protected HashMap<String , Object> initialValue() {
            if (logger.isInfoEnabled()) {
                logger.info(Thread.currentThread().getName());
            }
            return new HashMap<String , Object>(4);
        }
    };
}
