package cn.zj.easynet.device.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记缓存domain时作为key值的字段，同一个domain可能有多个key（对应数据库中的联合主键）
 * 
 * @author liyalong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CacheKey {
}
