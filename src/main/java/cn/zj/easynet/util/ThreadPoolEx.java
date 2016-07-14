package cn.zj.easynet.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/***
 * 修改以前固定线程池，改成伸缩线程池  guoj 20150910
 * 
 */
public class ThreadPoolEx {
	private static Logger logger = Logger.getLogger(ThreadPoolEx.class);
	private static int worker = Runtime.getRuntime().availableProcessors();;
	private static int corePoolSize = worker;
	private static int maxPoolSize = worker * 50;
    private static int keepAliveTime = 300;
	private static int maxBlockQueueSize = worker*2;
	static{
	    logger.info("ThreadPoolEx thread pool info: corePoolSize = "
	        + corePoolSize + ", maxPoolSize = " + maxPoolSize
	        + ", maxBlockQueueSize = " + maxBlockQueueSize);
	}
	// 超出负载后的的执行线程
    private static final ExecutorService es = Executors.newFixedThreadPool(worker);
    
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
    		keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(
                maxBlockQueueSize), new RejectedExecutionHandler() {
                public void rejectedExecution(Runnable r,
                    ThreadPoolExecutor executor) {
                	logger.error("Exceed max server process capacity. task execute on thread excutor");
                    es.execute(r);
                }
            });

	public static void execute(Runnable task) {
		executor.execute(task);
	}
	
	public static ExecutorService getExecutor() {
		return executor;
	}
}
