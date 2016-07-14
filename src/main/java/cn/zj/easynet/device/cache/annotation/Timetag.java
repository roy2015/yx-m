package cn.zj.easynet.device.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记domain中的时间戳字段，用于domain列表的增量更新
 * 
 * @author liyalong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Timetag {
}
