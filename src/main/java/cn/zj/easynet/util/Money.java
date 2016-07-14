package cn.zj.easynet.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

/**
 * If you see these comments as unreadable characters, please switch the encoding to "UTF-8".
 * <p>
 * 货币对象.
 * <p>
 * 支持符合ISO 4217 标准的国际币种，以及非标准的CNH，但不支持小数位大于等于4的币种.
 * <p>
 * 内部处理逻辑
 * <ul>
 * <li>0-3位小数位的币种，存储单位为<strong>厘</strong>，即调用以厘为单位的构造方法，或者调用getLi/setLi方法时，会做乘除1000操作，并对小数位进行四舍五入</li>
 * <li>大于或等于4位小数位的币种，不支持，会抛出 IllegalArgumentException</li>
 * </ul>
 * <p>
 * 实现功能
 * <ul>
 * <li>默认<strong>人民币</strong>构造器：new Money()</li>
 * <li>以厘为单位的<strong>人民币</strong>构造器：new Money(long li)</li>
 * <li>以元为单位的<strong>人民币</strong>构造器：new Money(String yuanAmount)</li>
 * <li>以厘为单位的<strong>指定币种</strong>构造器：new Money(String currencyCode, long li)</li>
 * <li>以元为单位的<strong>指定币种</strong>构造器：new Money(String currencyCode,String yuanAmount)</li>
 * <li>getter/setter</li>
 * <li>同币种金额加法运算，将返回一个新的对象</li>
 * <li>同币种金额减法运算，将返回一个新的对象</li>
 * <li>同币种金额乘法运算，将返回一个新的对象</li>
 * <li>同币种金额除法运算，将返回一个新的对象</li>
 * <li>同币种金额大小比较运算</li>
 * <li>两币种间的汇兑运算</li>
 * <li>定制化字符串输出：toCustomizedString(boolean isWithCurrencyCode, boolean isWithThousandSplit)</li>
 * </ul>
 * 
 * @author liuxf 2015年7月22日
 */
public class Money implements Serializable, Comparable<Money> {

    private static final long       serialVersionUID      = 2084574150755995748L;

    /**
     * 缺省币种代码，CNY（人民币）
     */
    private static final String     DEFAULT_CURRENCY_CODE = "CNY";

    /**
     * 缺省的取整模式，为<code>BigDecimal.ROUND_HALF_UP</code>
     */
    private static final int        DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    /**
     * 值为1000的BigDecimal常量
     */
    private static final BigDecimal THOUSAND              = BigDecimal.valueOf(1000L);

    /**
     * CNH
     */
    private static final String     CNH                   = "CNH";

    /**
     * 金额
     */
    private BigDecimal              yuanAmount;

    /**
     * 币种
     */
    private String                  currencyCode;

    // ======================================================
    // ================== 构造方法区 ==========================
    // ======================================================

    /**
     * <strong>人民币</strong>构造器，金额为0.00
     */
    public Money() {
        this.currencyCode = DEFAULT_CURRENCY_CODE;
        this.yuanAmount = BigDecimal.valueOf(0L, 2);
    }

    /**
     * <strong>人民币</strong>构造器，金额单位为厘
     * <p>
     * 厘---->元转换计算方法：先将li除以1000，再按照保留2位小数进行四舍五入，示例：
     * <ul>
     * <li>li=1000=1.00 元=1000 厘</li>
     * <li>li=1004=1.00 元=1000 厘</li>
     * <li>li=1005=1.01 元=1010 厘</li>
     * </ul>
     * 
     * @param li 以<strong>厘</strong>为单位的金额
     */
    public Money(long li) {
        this.currencyCode = DEFAULT_CURRENCY_CODE;
        Currency currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
        this.yuanAmount = getLegalAmountFromLi(currency, li);
    }

    /**
     * <strong>人民币</strong>构造器，金额单位为元
     * <p>
     * 元---->厘转换计算方法：先对amount进行四舍五入，再乘以1000，示例：
     * <ul>
     * <li>yuanAmount="1"=1.00 元=1000 厘</li>
     * <li>yuanAmount="1.0"=1.00 元=1000 厘</li>
     * <li>yuanAmount="1.05"=1.05 元=1050 厘</li>
     * <li>yuanAmount="1.004"=1.00 元=1000 厘</li>
     * <li>yuanAmount="1.005"=1.01 元=1010 厘</li>
     * </ul>
     * 
     * @param yuanAmount 以<strong>元</strong>为单位的金额
     * @exception NullPointerException 如果 amount 为 null
     * @exception NumberFormatException 如果 amount 不是数字
     */
    public Money(String yuanAmount) {
        this.currencyCode = DEFAULT_CURRENCY_CODE;
        Currency currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
        this.yuanAmount = getLegalAmountFromStringAmount(currency, yuanAmount);
    }

    /**
     * <strong>指定币种</strong> 构造器，金额单位为厘
     * <p>
     * 厘---->元转换计算方法：先将li除以1000，再按照保留currency应有的小数位进行四舍五入，示例：
     * <ul>
     * <li>无小数货币，[currencyCode=JPY, li=1000]=1 JPY=1000 li</li>
     * <li>无小数货币，[currencyCode=JPY, li=1400]=1 JPY=1000 li</li>
     * <li>无小数货币，[currencyCode=JPY, li=1500]=2 JPY=2000 li</li>
     * <li>2位小数货币，[currencyCode=USD, li=1000]=1.00 USD=1000 li</li>
     * <li>2位小数货币，[currencyCode=USD, li=1004]=1.00 USD=1000 li</li>
     * <li>2位小数货币，[currencyCode=USD, li=1005]=1.01 USD=1010 li</li>
     * <li>3位小数货币，[currencyCode=KWD, li=1000]=1.000 KWD=1000 li</li>
     * <li>3位小数货币，[currencyCode=KWD, li=1004]=1.004 KWD=1004 li</li>
     * <li>3位小数货币，[currencyCode=KWD, li=1005]=1.005 KWD=1005 li</li>
     * <li>4位小数货币，<strong>不支持</strong>，IllegalArgumentException</li>
     * </ul>
     * 
     * @param currencyCode ISO 4217 标准币种代码 或者 CNH
     * @param li 以<strong>厘</strong>为单位的金额
     * @exception NullPointerException 如果currencyCode为 null
     * @exception IllegalArgumentException 如果 currencyCode不被ISO 4217标准支持
     * @exception IllegalArgumentException 如果currency所代表的币种的小数位大于等于4
     */
    public Money(String currencyCode, long li) {
        if (CNH.equals(currencyCode)) {
            this.currencyCode = CNH;
            BigDecimal amountTmp = new BigDecimal(li);
            this.yuanAmount = amountTmp.divide(THOUSAND, 2, DEFAULT_ROUNDING_MODE);
        } else {
            Currency currency = Currency.getInstance(currencyCode);
            this.currencyCode = currency.getCurrencyCode();
            this.yuanAmount = getLegalAmountFromLi(currency, li);
        }
    }

    /**
     * <strong>指定币种</strong> 构造器，金额单位为元
     * <p>
     * 元---->厘转换计算方法：先对amount保留币种应有的小数位进行四舍五入，再乘以1000，示例：
     * <ul>
     * <li>无小数货币，[currencyCode=JPY, yuanAmount="1"]=1 JPY=1000 li</li>
     * <li>无小数货币，[currencyCode=JPY, yuanAmount="1.4"]=1 JPY=1000 li</li>
     * <li>无小数货币，[currencyCode=JPY, yuanAmount="1.5"]=2 JPY=2000 li</li>
     * <li>2位小数货币，[currencyCode=USD, yuanAmount="1"]=1.00 USD=1000 li</li>
     * <li>2位小数货币，[currencyCode=USD, yuanAmount="1.0"]=1.00 USD=1000 li</li>
     * <li>2位小数货币，[currencyCode=USD, yuanAmount="1.04"]=1.04 USD=1040 li</li>
     * <li>2位小数货币，[currencyCode=USD, yuanAmount="1.05"]=1.05 USD=1050 li</li>
     * <li>2位小数货币，[currencyCode=USD, yuanAmount="1.054"]=1.05 USD=1050 li</li>
     * <li>2位小数货币，[currencyCode=USD, yuanAmount="1.055"]=1.06 USD=1060 li</li>
     * <li>4位小数货币，<strong>不支持</strong>，IllegalArgumentException</li>
     * </ul>
     * 
     * @param currency ISO 4217 标准币种代码 或者 CNH
     * @param yuanAmount 以元为单位的金额
     * @exception NullPointerException 如果currencyCode为 null
     * @exception NullPointerException 如果amount为 null
     * @exception NumberFormatException 如果amount不是数字
     * @exception IllegalArgumentException 如果currencyCode不被ISO 4217标准支持
     * @exception IllegalArgumentException 如果currencyCode所代表的币种的小数位大于等于4
     */
    public Money(String currencyCode, String yuanAmount) {
        if (CNH.equals(currencyCode)) {
            this.currencyCode = CNH;
            BigDecimal amountTmp = new BigDecimal(yuanAmount);
            this.yuanAmount = amountTmp.divide(BigDecimal.ONE, 2, DEFAULT_ROUNDING_MODE);
        } else {
            Currency currency = Currency.getInstance(currencyCode);
            this.currencyCode = currency.getCurrencyCode();
            this.yuanAmount = getLegalAmountFromStringAmount(currency, yuanAmount);
        }
    }

    // ======================================================
    // ================== getter/setter区 ======================
    // ======================================================

    /**
     * 获取币种code
     * 
     * @return
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * 设置币种code, <strong>如果币种的小数位不同，那么调用此方法后，相应的金额数值会重新计算</strong>
     * 
     * @throws NullPointerException 如果currencyCode为null
     * @throws IllegalArgumentException 如果currencyCode不被ISO 4217标准支持
     * @throws IllegalArgumentException 如果币种的小数位大于等于4
     * @param currencyCode
     */
    public void setCurrencyCode(String currencyCode) {
        int currentCurrencyDigits = 0;// 当前币种对象的小数位
        if (CNH.equals(this.currencyCode)) {
            currentCurrencyDigits = 2;
        } else {
            Currency currentCurrency = Currency.getInstance(this.currencyCode);// 当前对象的币种
            currentCurrencyDigits = currentCurrency.getDefaultFractionDigits();
        }

        int passInCurrencyDigits = 0;// 传入币种对象的小数位
        if (CNH.equals(currencyCode)) {
            passInCurrencyDigits = 2;
        } else {
            Currency passInCurrency = Currency.getInstance(currencyCode);
            passInCurrencyDigits = passInCurrency.getDefaultFractionDigits();
        }

        if (currentCurrencyDigits == passInCurrencyDigits) {
            this.currencyCode = currencyCode;
        } else {
            if (passInCurrencyDigits >= 4) {
                throw new IllegalArgumentException("Currency  is unsupported: " + currencyCode);
            }
            this.yuanAmount = yuanAmount.divide(BigDecimal.ONE, passInCurrencyDigits, DEFAULT_ROUNDING_MODE);
            this.currencyCode = currencyCode;
        }
    }

    /**
     * 获取币种的厘值
     * 
     * @return
     */
    public long getLi() {
        return this.yuanAmount.multiply(THOUSAND).longValue();
    }

    /**
     * 设置币种的<strong>厘</strong>值
     * 
     * @param li 以<strong>厘</strong>为单位的数值
     */
    public void setLi(long li) {
        if (CNH.equals(currencyCode)) {
            this.yuanAmount = BigDecimal.valueOf(li).divide(THOUSAND, 2, DEFAULT_ROUNDING_MODE);
        } else {
            Currency currentCurrency = Currency.getInstance(this.currencyCode);// 当前对象的币种
            this.yuanAmount = BigDecimal.valueOf(li).divide(THOUSAND, currentCurrency.getDefaultFractionDigits(),
                                                            DEFAULT_ROUNDING_MODE);
        }

    }

    /**
     * 获取币种的<strong>元</strong>值字符串
     * 
     * @return
     */
    public String getYuanAmount() {
        return this.yuanAmount.toString();
    }

    // ======================================================
    // ================== 算术运算区 =========================
    // ======================================================

    /**
     * 同币种金额加法运算，<strong>将返回一个新的对象</strong>
     * 
     * @param other 作为加数的货币对象
     * @exception IllegalArgumentException 如果本货币对象与另一货币对象币种不同
     * @return 相加后的结果
     */
    public Money add(Money other) {
        assertSameCurrencyAs(other);
        BigDecimal amountTmp = this.yuanAmount.add(other.yuanAmount);
        return new Money(currencyCode, amountTmp);
    }

    /**
     * 同币种金额减法运算，<strong>将返回一个新的对象</strong>
     * 
     * @param other 作为减数的货币对象。
     * @exception IllegalArgumentException 如果本货币对象与另一货币对象币种不同
     * @return 相减后的结果
     */
    public Money subtract(Money other) {
        assertSameCurrencyAs(other);
        BigDecimal amountTmp = this.yuanAmount.subtract(other.yuanAmount);
        return new Money(currencyCode, amountTmp);
    }

    /**
     * 同币种金额乘法运算，<strong>将返回一个新的对象</strong>
     * <p>
     * 因为现实中，没有金额*金额的场景，只有金额*系数或者比例，所以这里的参数为double，非Money
     * 
     * @param factor 系数 或者 比例
     * @return
     */
    public Money multiply(double factor) {
        BigDecimal amountTmp = this.yuanAmount.multiply(BigDecimal.valueOf(factor));
        return new Money(currencyCode, amountTmp);
    }

    /**
     * 同币种金额除法运算，<strong>将返回一个新的对象</strong>
     * <p>
     * 因为现实中，没有金额/金额的场景，只有金额/系数或者比例，所以这里的参数为double，非Money
     * 
     * @param factor 系数 或者 比例
     * @return
     */
    public Money divide(double factor) {
        BigDecimal amountTmp = null;
        if (CNH.equals(currencyCode)) {
            amountTmp = this.yuanAmount.divide(BigDecimal.valueOf(factor), 2, DEFAULT_ROUNDING_MODE);

        } else {
            Currency currency = Currency.getInstance(currencyCode);
            amountTmp = this.yuanAmount.divide(BigDecimal.valueOf(factor), currency.getDefaultFractionDigits(),
                                               DEFAULT_ROUNDING_MODE);
        }
        return new Money(currencyCode, amountTmp);
    }

    /**
     * 带币种的，以<strong>元</strong>为单位的字符串，示例："USD 1.00"
     * 
     * @return
     */
    @Override
    public String toString() {
        return this.currencyCode + " " + yuanAmount.toString();
    }

    /**
     * 定制化字符串，示例：
     * <ul>
     * Money cnyMoney = new Money("10000.01");
     * <li>toCustomizedString(true, false)="CNY 10000.01"</li>
     * <li>toCustomizedString(true, true)="CNY 10,000.01"</li>
     * <li>toCustomizedString(false, true)="10,000.01"</li>
     * <li>toCustomizedString(false, false)="10000.01"</li>
     * </ul>
     * 
     * @param isWithCurrencyCode 是否需要币种
     * @param isWithThousandSplit 是否需要千分位分割
     * @return
     */
    public String toCustomizedString(boolean isWithCurrencyCode, boolean isWithThousandSplit) {
        String amountStr = null;
        if (isWithThousandSplit) {
            NumberFormat nf = new DecimalFormat("#,###.###");
            amountStr = nf.format(this.yuanAmount.doubleValue());
        } else {
            amountStr = getYuanAmount();
        }

        if (isWithCurrencyCode) {
            amountStr = this.currencyCode + " " + amountStr;
        }
        return amountStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Money other = (Money) obj;
        if (yuanAmount == null) {
            if (other.yuanAmount != null) return false;
        } else if (!yuanAmount.equals(other.yuanAmount)) return false;
        if (currencyCode == null) {
            if (other.currencyCode != null) return false;
        } else if (!currencyCode.equals(other.currencyCode)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((yuanAmount == null) ? 0 : yuanAmount.hashCode());
        result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
        return result;
    }

    /**
     * 同币种货币金额比较
     * 
     * @param other 另一对象。
     * @return -1表示小于，0表示等于，1表示大于
     * @exception IllegalArgumentException 如果待比较货币对象与本货币对象的币种不同
     */
    public int compareTo(Money other) {
        assertSameCurrencyAs(other);
        long thisLongValue = getLi();
        long otherLongValue = other.getLi();
        if (thisLongValue < otherLongValue) {
            return -1;
        } else if (thisLongValue == otherLongValue) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 同币种货币金额比较
     * 
     * @param other 另一对象。
     * @return true表示大于，false表示不大于（小于等于）。
     * @exception IllegalArgumentException 如果待比较货币对象与本货币对象的币种不同
     */
    public boolean greaterThan(Money other) {
        return compareTo(other) > 0;
    }

    /**
     * 货币汇兑
     * <p>
     * 计算方法：source*exchangeRate，再根据targetCurrencyCode的小数位进行四舍五入
     * 
     * @param source 源币种
     * @param exchangeRate 汇率
     * @param targetCurrencyCode 目标币种
     * @return 汇兑后的货币对象
     * @throws NullPointerException 如果source为null
     * @throws NullPointerException 如果targetCurrencyCode为null
     * @throws IllegalArgumentException 如果targetCurrencyCode为>=4位小数货币
     */
    public static Money foreignExchange(Money source, double exchangeRate, String targetCurrencyCode) {
        BigDecimal sourceAmount = new BigDecimal(source.getYuanAmount());
        BigDecimal amountTmp = sourceAmount.multiply(BigDecimal.valueOf(exchangeRate));
        return new Money(targetCurrencyCode, amountTmp);
    }

    /**
     * 货币汇兑，通过除操作实现
     * <p>
     * 计算方法：source*exchangeRate，再根据targetCurrencyCode的小数位进行四舍五入
     * 
     * @param source 源币种
     * @param exchangeRate 汇率
     * @param targetCurrencyCode 目标币种
     * @return 汇兑后的货币对象
     * @throws NullPointerException 如果source为null
     * @throws NullPointerException 如果targetCurrencyCode为null
     * @throws IllegalArgumentException 如果targetCurrencyCode为>=4位小数货币
     */
    public static Money foreignExchangeByDivide(Money source, double exchangeRate, String targetCurrencyCode) {
        int targetCurrencyDigits = 0;// 目标币种对象的小数位
        if ("CNH".equals(targetCurrencyCode)) {
            targetCurrencyDigits = 2;
        } else {
            Currency targetCurrency = Currency.getInstance(targetCurrencyCode);
            targetCurrencyDigits = targetCurrency.getDefaultFractionDigits();
        }
        if (targetCurrencyDigits >= 4) {
            throw new IllegalArgumentException("Currency  is unsupported: " + targetCurrencyCode);
        }

        BigDecimal sourceAmount = new BigDecimal(source.getYuanAmount());
        BigDecimal amountTmp = sourceAmount.divide(BigDecimal.valueOf(exchangeRate), targetCurrencyDigits,
                                                   DEFAULT_ROUNDING_MODE);
        return new Money(targetCurrencyCode, amountTmp);
    }

    // 内部方法 ===================================================

    /**
     * 判断是否同币种
     * 
     * @param other 另一货币对象
     * @exception IllegalArgumentException 如果本货币对象与另一货币对象币种不同
     */
    private void assertSameCurrencyAs(Money other) {
        if (!currencyCode.equals(other.getCurrencyCode())) {
            throw new IllegalArgumentException("The currency is different.");
        }
    }

    /**
     * 根据币种，以厘单位的金额，获取正确的以元为单位的BigDecimal
     * 
     * @param currency 币种
     * @param li 以厘单位的金额
     * @return 以元为单位的BigDecimal
     * @throws IllegalArgumentException 如果币种的小数位大于等于4
     */
    private BigDecimal getLegalAmountFromLi(Currency currency, long li) {
        // 校验币种小数位数是否支持
        int digits = currency.getDefaultFractionDigits();
        if (digits >= 4) {
            throw new IllegalArgumentException("Currency  is unsupported: " + currency.getCurrencyCode());
        }

        BigDecimal amountTmp = new BigDecimal(li);
        return amountTmp.divide(THOUSAND, currency.getDefaultFractionDigits(), DEFAULT_ROUNDING_MODE);

    }

    /**
     * 根据币种，以元为单位的金额，获取正确的以元为单位的BigDecimal
     * 
     * @param currency 币种
     * @param amount 以元单位的金额字符串
     * @return 以元为单位的BigDecimal
     * @throws IllegalArgumentException 如果币种的小数位>=4
     */
    private BigDecimal getLegalAmountFromStringAmount(Currency currency, String amount) {
        // 校验币种小数位数是否支持
        int digits = currency.getDefaultFractionDigits();
        if (digits >= 4) {
            throw new IllegalArgumentException("Currency  is unsupported: " + currency.getCurrencyCode());
        }

        BigDecimal amountTmp = new BigDecimal(amount);
        return amountTmp.divide(BigDecimal.ONE, currency.getDefaultFractionDigits(), DEFAULT_ROUNDING_MODE);
    }

    /**
     * 内部构造方法
     * 
     * @param currency
     * @param amount
     */
    private Money(String currencyCode, BigDecimal amount) {
        if (CNH.equals(currencyCode)) {
            this.currencyCode = CNH;
            this.yuanAmount = amount.divide(BigDecimal.ONE, 2, DEFAULT_ROUNDING_MODE);
        } else {
            this.currencyCode = currencyCode;
            Currency currency = Currency.getInstance(currencyCode);
            this.yuanAmount = amount.divide(BigDecimal.ONE, currency.getDefaultFractionDigits(), DEFAULT_ROUNDING_MODE);
        }
    }
}
