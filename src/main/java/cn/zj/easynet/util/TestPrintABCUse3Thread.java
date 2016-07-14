package cn.zj.easynet.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 类TestReentrantLock.java的实现描述：TODO 类实现描述 
 * @author Administrator 2016年4月20日 下午4:07:00
 * 
 * 
 */
public class TestPrintABCUse3Thread {

	public volatile static boolean isStart = true;
	private volatile static int round=0;//轮次
	private volatile static String current;
	private volatile static LinkedBlockingQueue<String> blockQueue = new  LinkedBlockingQueue<String>();
	
	private ReentrantLock lock;
	private Condition cCond;
	private Condition bCond;
	private Condition aCond;
	
	private String printChar;

	public TestPrintABCUse3Thread(ReentrantLock lock, Condition cCond, Condition bCond, Condition aCond, String printChar) {
        super();
        this.lock = lock;
        this.cCond = cCond;
        this.bCond = bCond;
        this.aCond = aCond;
        this.printChar = printChar;
    }

	public void print(String charStr){
	    if(round < 10){
	        System.out.print(  charStr );
	    }
	}
	
    public void testRun() {
	    for(;round <10;){
	        lock.lock(); // block until condition holds
	        try {
	            if("A".equalsIgnoreCase(printChar)){
	                if (isStart) {
	                    print(printChar);
	                    isStart=false;
	                    current = "A";
	                } else {//不是开始，等待C条件
	                    if(!"C".equalsIgnoreCase(current)){
	                        blockQueue.put("A");
	                        cCond.await();
	                        blockQueue.remove("A");
	                    }
	                    print(printChar);
	                    current = "A";
	                    if(blockQueue.contains("B")){
	                        aCond.signal();
	                    }
	                }
	                
	            }else if("B".equalsIgnoreCase(printChar)){
	                if(!"A".equalsIgnoreCase(current)){
	                    blockQueue.put("B");
                        aCond.await();
                        blockQueue.remove("B");
                    }
	                print(printChar);
                    current = "B";
                    if(blockQueue.contains("C")){
                        bCond.signal();
                    }
	                
	            }else if("C".equalsIgnoreCase(printChar)){
	                if(!"B".equalsIgnoreCase(current)){
	                    blockQueue.put("C");
                        bCond.await();
                        blockQueue.remove("C");
                    }
	                print(printChar);
                    round ++;
                    current = "C";
                    if(blockQueue.contains("A")){
                        cCond.signal();
                    }
	                
	            }else{}
	            
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        } finally {
	            lock.unlock();
	        }
	    }
	}
    
    

	public static void main(String[] args) throws Exception {
	    for(int i=1;i<=10;i++){
	        System.out.print("ABC");
	    }
	    System.out.println("");
	    
		ReentrantLock lock = new ReentrantLock();
		Condition cCond = lock.newCondition();
		Condition bCond = lock.newCondition();
		Condition aCond = lock.newCondition();
		
		final TestPrintABCUse3Thread lock1 = new TestPrintABCUse3Thread(lock, cCond, bCond, aCond,"A");
		final TestPrintABCUse3Thread lock2 = new TestPrintABCUse3Thread(lock, cCond, bCond, aCond,"B");
		final TestPrintABCUse3Thread lock3 = new TestPrintABCUse3Thread(lock, cCond, bCond, aCond,"C");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				lock3.testRun();
			}
		}).start();
		
//		TimeUnit.SECONDS.sleep(2);
		
		new Thread(new Runnable() {
            @Override
            public void run() {
                lock1.testRun();
            }
        }).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				lock2.testRun();
			}
		}).start(); 
		
		TimeUnit.SECONDS.sleep(10);
	}

}
