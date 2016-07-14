package cn.zj.easynet.common.marshal;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.zj.easynet.common.pack.Pack;
import cn.zj.easynet.common.pack.Unpack;
import cn.zj.easynet.common.util.EmptyPropertyException;
import cn.zj.easynet.common.util.SysDateTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 打包时使用跟Property相同的格式
 */
public abstract class LiteDomain implements Marshallable {
    public static Logger logger = Logger.getLogger(LiteDomain.class);

    // 用于缓存“数据库查询结果为空”的状态
    public static final Property NULL = new Property();

    // 用于缓存子类的Field列表，避免每次都要现取，对app可节省约10%-15%的内存开销，降低相应的minor gc频率
    private static ThreadLocal<Map<String, Field[]>> domainFieldsCacheMap = new ThreadLocal<Map<String, Field[]>>();

    // 用于缓存通过annotation取到的domainTagId，减少反射耗时，marshal/unmarshal速度可提升20倍以上
    // 数据结构：map<className, map<fieldName, id>>
    // TODO: 使用asm动态生成字节码，进一步减少反射耗时
    private static ThreadLocal<Map<String, Map<String, Integer>>> domainTagIdCacheMap = new ThreadLocal<Map<String, Map<String, Integer>>>();

    private Field[] getDomainFields(Class<?> clazz) {
        Map<String, Field[]> map = domainFieldsCacheMap.get();
        if (map == null) {
            map = new HashMap<String, Field[]>();
            domainFieldsCacheMap.set(map);
        }

        String className = clazz.getName();
        Field[] fields = map.get(className);
        if (fields == null) {
            fields = clazz.getDeclaredFields();
            map.put(className, fields);
        }

        return fields;
    }

    private Integer getDomainTagId(Class<?> clazz, Field field) {
        Map<String, Map<String, Integer>> map = domainTagIdCacheMap.get();
        if (map == null) {
            map = new HashMap<String, Map<String, Integer>>();
            domainTagIdCacheMap.set(map);
        }

        String className = clazz.getName();
        Map<String, Integer> subMap = map.get(className);
        if (subMap == null) {
            subMap = new HashMap<String, Integer>();
            map.put(className, subMap);
        }

        String fieldName = field.getName();
        if (subMap.containsKey(fieldName)) {
            Integer id = subMap.get(fieldName);
            return id;
        } else {
            // 该方法是反射调用里最慢的，比getName()之类的调用慢了两三个数量级，因此需要对结果作缓存
            DomainTag tag = field.getAnnotation(DomainTag.class);
            if (tag == null) {
                subMap.put(fieldName, null); // null也要缓存
                return null;
            } else {
                int id = tag.id();
                subMap.put(fieldName, id);
                return id;
            }
        }
    }

    @Override
    public void marshal(Pack pack) {
        Class<?> clazz = this.getClass();
        Field[] fields = getDomainFields(clazz);

        int size = 0;
        int pos = pack.getPosition();
        pack.putInt(fields.length);

        for (Field field: fields) {
            field.setAccessible(true);
            Integer id = getDomainTagId(clazz, field);
            if (id == null)
                continue;

            try {
                Object obj = field.get(this);
                if (obj == null)
                    continue;

                pack.putInt(id);

                Class<?> objClazz = field.getType();
                if (objClazz == byte[].class) {
                    pack.putVarbin((byte[]) obj);
                } else {
                    pack.putVarstr(obj.toString()); // 数字也作为字符串来存储
                }

                size++;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Marshall error");
            }
        }
        if (size != fields.length)
            pack.replaceInt(pos, size);
    }

    @Override
    public void unmarshal(Unpack unpack) {
        Property p = new Property();
        unpack.popMarshallable(p);
        if (p == null || p.size() == 0) {
            throw new EmptyPropertyException();
        }

        Class<?> clazz = this.getClass();
        Field[] fields = getDomainFields(clazz);

        for (Field field: fields) {
            field.setAccessible(true);
            Integer id = getDomainTagId(clazz, field);
            if (id == null)
                continue;

            try {
                Class<?> objClazz = field.getType();

                // bytes
                if (objClazz == byte[].class) {
                    byte[] value = p.getBytes(id);
                    field.set(this, value);
                }
                // string
                else {
                    String value = p.get(id);
                    if (value == null) {
                        field.set(this, null);
                        continue;
                    }

                    if (objClazz == Integer.class) {
                        field.set(this, Integer.parseInt(value));
                    } else if (objClazz == Long.class) {
                        field.set(this, Long.parseLong(value));
                    } else if (objClazz == Short.class) {
                        field.set(this, Short.parseShort(value));
                    } else if (objClazz == Byte.class) {
                        field.set(this, Byte.parseByte(value));
                    } else if (objClazz == Double.class) {
                        field.set(this, Double.parseDouble(value));
                    } else {
                        field.set(this, value);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("Unarshall error");
            }
        }
    }

    /**
     * 注：仅用于在cache-monitor等工具内展示时提高可读性，不用于任何业务逻辑
     */
    public String toJSONString() {
        return JSON.toJSONString(toJSONObject(), true);
    }

    protected JSONObject toJSONObject() {
        String str = JSON.toJSONString(this);
        JSONObject jo = JSON.parseObject(str);

        Object timetag = jo.get("timetag");
        if (timetag != null)
            jo.put("timetag", getReadableTimetag((Integer) timetag));

        return jo;
    }

    protected String getReadableTimetag(Integer timetag) {
        return SysDateTime.getReadableTimetag(timetag);
    }
}
