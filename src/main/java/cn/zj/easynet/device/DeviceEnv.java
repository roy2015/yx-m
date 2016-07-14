package cn.zj.easynet.device;


import org.apache.log4j.Logger;


public class DeviceEnv {
    private static Logger logger = Logger.getLogger(DeviceEnv.class);

    /*
     * 配置参数
     */
    public static boolean isCacheEnabled = false; // 是否使用cache

    public static boolean isLocalCacheEnabled = false; // 是否使用cache

    public static boolean KeywordFilterEnable = true; // 是否开启关键字过滤

    public static int httpTimeoutThreshold = 1000 * 60; // 访问外部http连接的超时值

    /*
     * 异步访问第三方HTTP连接的超时值
     */
    public static int asyncHttpTimeoutThreshold = 5000;

    /*
     * 异步HTTP的worker数量默认是CPU线程数，一个worker里的selector处理多个conn。
     * 与同步HTTP的每个conn要消耗一个线程相比系统开销大大降低。所以异步HTTP的conn的数量限制可以放宽。
     * 假如所有conn都在等待5秒超时，服务器每秒创建6400个conn，每个conn上下文消耗10K。
     * 以此推算得出内存消耗=5*6400*10K=312.5M。
     */
    public static int asyncHttpMaxConnTotal = 32000;

    /*
     * 考虑到不同route使用异步HTTP单例，每个route的conn限制设为最大值的一半。
     * 如果某一route请求量确实很大，请创建并使用新的异步HTTP的实例。
     */
    public static int asyncHttpMaxConnPerRoute = 16000;

    /*
     * 外部网络设备接口
     */

}
