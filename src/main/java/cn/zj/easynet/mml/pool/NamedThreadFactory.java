package cn.zj.easynet.mml.pool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class NamedThreadFactory implements ThreadFactory
{
	static Logger logger = Logger.getLogger(NamedThreadFactory.class);
    final AtomicInteger threadNumber = new AtomicInteger(0);
    final ThreadGroup   group;
    final String        namePrefix;
    
    public NamedThreadFactory(String threadPoolName)
    {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = threadPoolName + "-pool-thread-"; 
    }
    
    public NamedThreadFactory(String threadPoolName,String middle)
    {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        if(middle == null || middle.trim().isEmpty())
        {
            namePrefix = threadPoolName + "-";
        }
        else 
            namePrefix = threadPoolName + "-" + middle + "-";
    }
    
    public Thread newThread(Runnable r)
    {
        int index = threadNumber.incrementAndGet();
        Thread t = new Thread(group, r, namePrefix + index, 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        
        logger.info("create thread, name:" + namePrefix + index);
          
        return t;
    }
}
