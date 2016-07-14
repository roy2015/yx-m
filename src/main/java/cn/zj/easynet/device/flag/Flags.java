package cn.zj.easynet.device.flag;

import cn.zj.easynet.common.marshal.LiteDomain;

/**
 * 各类标记的基类
 * <p>
 * 非线程安全
 * 
 * @author jiangzhouyun@corp.netease.com
 */
public abstract class Flags extends LiteDomain {
    /**
     * 所有标记被0
     */
    public static final Integer NONE_FLAG = 0x0;

    /**
     * 所有标记被设置1
     * <p>
     * 为了避免底层数据库的int值符号问题, 一般建议使用的bit位保留最高位不用, 即可用范围是: 0-31
     */
    public static final Integer ALL_FLAG = 0xFFFFFFFF;

    /**
     * int类型的标记值
     */
    protected Integer flagsValue = NONE_FLAG;

    /**
     * 构造函数
     */
    public Flags() {
        flagsValue = NONE_FLAG;
    }

    /**
     * 指定初始数值的构造函数
     * 
     * @param flags
     */
    public Flags(Integer flags) {
        this.flagsValue = flags;
    }

    /**
     * 获取所有标记数值
     * 
     * @return
     */
    public Integer getFlagsValue() {
        return flagsValue;
    }

    /**
     * 设置所有标记数值
     * 
     * @param flagsValue
     */
    public void setFlagsValue(Integer flagsValue) {
        this.flagsValue = flagsValue;
    }

    /**
     * 批量设置指定标记
     * 
     * @param toMarkFlags
     */
    public void markFlags(Integer toMarkFlags) {
        flagsValue |= toMarkFlags;
    }

    /**
     * 批量取消指定标记
     * 
     * @param toUnmarkFlags
     *            待取消的标记
     */
    public void unmarkFlags(Integer toUnmarkFlags) {
        flagsValue &= (~toUnmarkFlags);
    }

    /**
     * 设置指定序号标记的值
     * 
     * @param flagIndex
     *            标记序号
     * @param mark
     *            true=设置标记;false=取消标记
     */
    public void setFlag(int flagIndex, boolean mark) {
        if (flagIndex >= 31) {
            throw new IllegalArgumentException("flag index out of bounds");
        }
        if (mark) {
            flagsValue |= (1 << flagIndex);
        } else {
            flagsValue &= (~(1 << flagIndex));
        }
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder(64).append('{');
        for (int i = 0; i < 32; i++) {
            if ((flagsValue & (1 << i)) == (1 << i)) {
                buff.append(i).append(":1,");
            }
        }
        if (buff.charAt(buff.length() - 1) == ',') {
            buff.setCharAt(buff.length() - 1, '}');
        } else {
            buff.append('}');
        }

        return buff.toString();
    }
}
