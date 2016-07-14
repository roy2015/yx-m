package cn.zj.easynet.common.marshal;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.msgpack.MessagePack;
import org.msgpack.packer.BufferPacker;
import org.msgpack.unpacker.Unpacker;

import cn.zj.easynet.common.pack.Pack;
import cn.zj.easynet.common.pack.PackException;
import cn.zj.easynet.common.pack.Unpack;
import cn.zj.easynet.common.pack.UnpackException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Property implements Marshallable {
    public Map<Integer, Value> props = new HashMap<Integer, Value>();

    public static Property unpack(Unpack p) {
        Property props = new Property();
        props.unmarshal(p);
        return props;
    }

    public void marshal(Pack p) {
        p.putInt(props.size());
        for (Iterator<Integer> it = iterator(); it.hasNext();) {
            int tag = it.next();
            p.putInt(tag);
            Value value = props.get(tag);
            p.putVarbin(value.bytes);
        }
    }

    public void unmarshal(Unpack p) {
        int cnt = p.popInt();
        for (int i = 0; i < cnt; ++i) {
            int tag = p.popInt();
            props.put(tag, new Value(p.popVarbin()));
        }
    }

    public boolean equals(Property prop) {
        return this.props.equals(prop.props);
    }

    public Iterator<Integer> iterator() {
        return props.keySet().iterator();
    }

    public String get(Integer tag) {
        Value v = props.get(tag);

        if (v != null) {
            return props.get(tag).toString();
        }
        return null;
    }

    public byte[] getBytes(Integer tag) {
        Value v = props.get(tag);
        if (v != null) {
            return v.bytes;
        }
        return null;
    }

    public void putBytes(Integer tag, byte[] bytes) {
        Value v = new Value(bytes);
        props.put(tag, v);
    }

    public void put(Integer tag, String value) {
        props.put(tag, new Value(value));
    }

    public int getInteger(Integer tag) {
        String value = get(tag);
        if (value == null || value.equals(""))
            return 0;
        return Integer.parseInt(value);
    }

    public void putInteger(Integer tag, int value) {
        props.put(tag, new Value(String.valueOf(value)));
    }

    public long getLong(Integer tag) {
        String value = get(tag);
        if (value == null || value.equals(""))
            return 0;
        return Long.parseLong(value);
    }

    public void putLong(Integer tag, long value) {
        props.put(tag, new Value(String.valueOf(value)));
    }

    public int size() {
        return props.size();
    }

    public void clear() {
        props.clear();
    }

    /**
     * 根据传入的keys，复制一个新的Property
     * 
     * @param keys
     * @return
     */
    public Property duplicate(Collection<Integer> keys, boolean isFillNull) {
        Property ret = new Property();
        for (Integer key: keys) {
            if (props.containsKey(key)) {
                ret.putValue(key, props.get(key));
            } else if (isFillNull) {
                ret.putValue(key, Value.EMPTY);
            }
        }
        return ret;
    }

    /**
     * 复制这个Property
     * 
     * @return
     */
    public Property duplicate() {
        Property ret = new Property();

        for (Map.Entry<Integer, Value> entry: props.entrySet()) {
            ret.putValue(entry.getKey(), entry.getValue());
        }

        return ret;
    }

    public void putValue(Integer key, Value value) {
        props.put(key, value);
    }

    public static byte[] fromString(String str) {
        try {
            return (str == null ? "" : str).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnpackException();
        }
    }

    public static String fromBytes(byte[] bytes) {
        try {
            if (bytes != null) {
                return new String(bytes, "utf-8");
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            throw new UnpackException();
        }
    }

    public void remove(Integer tag) {
        removeValue(tag);
    }

    public Value removeValue(Integer tag) {
        return props.remove(tag);
    }

    public String toString() {
        return props.toString();
    }

    public JSONObject toJSONObject() {
        JSONObject jo = new JSONObject(true);
        for (Map.Entry<Integer, Value> entry: props.entrySet()) {
            jo.put(String.valueOf(entry.getKey()), entry.getValue().toString());
        }
        return jo;
    }

    public JSONObject toJSONObject(final Map<String, String> readableKeyMap) {
        JSONObject jo = new JSONObject(true);
        for (Map.Entry<Integer, Value> entry: props.entrySet()) {
            String key = String.valueOf(entry.getKey());
            key = String.format("%s [%s]", key, readableKeyMap.get(key));
            jo.put(key, entry.getValue().toString());
        }
        return jo;
    }

    public String toJSONString() {
        return JSON.toJSONStringZ(toJSONObject(),
            SerializeConfig.getGlobalInstance(),
            SerializerFeature.PrettyFormat, SerializerFeature.QuoteFieldNames);
    }

    public Collection<String> values() {
        ArrayList<String> list = new ArrayList<String>(props.size());
        for (Value value: props.values()) {
            list.add(value.toString());
        }
        return list;
    }

    public boolean containsKey(int key) {
        return props.containsKey(key);
    }

    public final static class Value {
        static Value EMPTY = new Value("");

        private String str;

        private byte[] bytes;

        public Value(byte[] bytes) {
            super();
            setBytes(bytes);
        }

        public Value(String str) {
            super();
            set(str);
        }

        private void set(String str) {
            this.str = str;
            this.bytes = fromString(str);
        }

        private void setBytes(byte[] bytes) {
            this.str = null;
            if (bytes == null) {
                this.bytes = fromString("");
                this.str = "";
            } else {
                this.bytes = bytes;
            }
        }

        @Override
        public String toString() {
            if (str == null) {
                str = fromBytes(bytes);
            }
            return str;
        }

    }

    // -----------------------------------------------------
    // msgpack版的序列化/反序列化接口，仅用于在redis内存储在线状态/漫游消息
    // -----------------------------------------------------

    private static Logger logger = Logger.getLogger(Property.class);

    private static ThreadLocal<MessagePack> msgpack = new ThreadLocal<MessagePack>();

    private MessagePack getMsgPack() {
        MessagePack mp = msgpack.get();
        if (mp == null) {
            mp = new MessagePack();
            msgpack.set(mp);
        }
        return mp;
    }

    public byte[] msgPackSerialize() {
        BufferPacker packer = getMsgPack().createBufferPacker();

        try {
            // 注：序列化时采用手动构造map的方式，比packer.write(map)的性能高一倍
            packer.writeMapBegin(props.size());
            for (Map.Entry<Integer, Value> entry: props.entrySet()) {
                packer.write(entry.getKey());
                packer.write(entry.getValue().bytes);
            }
            packer.writeMapEnd();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new PackException(e.getMessage());
        }

        return packer.toByteArray();
    }

    public void msgPackDeserialize(byte[] bytes) {
        Unpacker up = getMsgPack().createBufferUnpacker(bytes);

        try {
            int size = up.readMapBegin();
            for (int i = 0; i < size; i++) {
                props.put(up.readInt(), new Value(up.readByteArray()));
            }
            up.readMapEnd();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UnpackException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Property p = new Property();
        p.put(1, null);
        p.put(2, "rrrr");
        p.put(3, "44");
        p.put(4, "555");
        p.put(5, "66666");
        // System.out.println(p);
        // System.out.println(p.getInteger(1));
        // System.out.println(p.getInteger(2));

        byte[] bytes = p.msgPackSerialize();
        Property p2 = new Property();
        p2.msgPackDeserialize(bytes);
        System.out.println(p2.toString());
    }
}
