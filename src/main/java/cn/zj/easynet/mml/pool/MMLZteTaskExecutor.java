package cn.zj.easynet.mml.pool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class MMLZteTaskExecutor {
	

	/**
	 * ==================================================================
	 * 
	 * ==================================================================
	 */
		public static ThreadPoolExecutor executor=null;
		static Logger logger = Logger.getLogger(TaskExecutor.class);

		public static void init(int minWorkerCount, int maxWorkerCount,
				long keepAliveTime, String name) {
			
			if(null!=executor){
				logger.warn("ReceiptExecutor.receiptExecutor the param is created");
				return;
			}
			
			executor = new ThreadPoolExecutor(minWorkerCount, maxWorkerCount,
					keepAliveTime, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory(
							name, null));
		}

		public static Future<?> submit(Runnable task)
				throws ServiceRuntimeException {
			if (executor == null)
				throw new ServiceRuntimeException(
						"Please init executor before submit task");

			return executor.submit(task);
		}

		public static <T> Future<T> submit(Callable<T> task)
				throws ServiceRuntimeException {
			if (executor == null)
				throw new ServiceRuntimeException(
						"Please init executor before submit task");

			return executor.submit(task);
		}

		public static <T> Future<T> submit(Runnable task, T result)
				throws ServiceRuntimeException {
			if (executor == null)
				throw new ServiceRuntimeException(
						"Please init executor before submit task");

			return executor.submit(task, result);
		}

		public static int getTaskQueueSize() {
			if (executor == null || executor.getQueue() == null) {
				return -1;
			}
			return executor.getQueue().size();
		}

		public static String getQueueInfo() {
			if (executor == null || executor.getQueue() == null) {
				return "N/A";
			}
			Queue<Runnable> queue = ((ThreadPoolExecutor) executor.getQueue())
					.getQueue();
			Iterator<Runnable> it = queue.iterator();
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			while (it.hasNext()) {
				Runnable runnable = (Runnable) it.next();
				String className = runnable.getClass().getSimpleName();
				int count = map.containsKey(className) ? map.get(className) : 0;
				map.put(className, ++count);
			}
			return map.toString();
		}
}
