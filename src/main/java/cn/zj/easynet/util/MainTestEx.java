package cn.zj.easynet.util;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainTestEx {

	//List在遍历的时候，如果被修改了会抛出java.util.ConcurrentModificationException错误
	public static void test1() throws Exception {
		List<String> a = new ArrayList<String>();  
        a.add("a");  
        a.add("b");  
        a.add("c");
        final ArrayList<String> list = new ArrayList<String>(  
                a);  
        Thread t = new Thread(new Runnable() {  
            int count = -1;  
  
            @Override  
            public void run() {  
                while (true) {  
                    list.add(count++ + "");  
//                	list.set(0, "a1");
                }  
            }  
        });  
        t.setDaemon(true);  
        t.start();  
        Thread.currentThread().sleep(3); 
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			System.out.println( (String) iterator.next() );
			
		}
//        for (String s : list) {  
//            System.out.println(s);  
//        }  
	}
	
	public static void test2() throws Exception {
		Future<Dto1> future = ThreadPoolEx.getExecutor().submit(new Callable<Dto1>() {
			@Override
			public Dto1 call() throws Exception {
				// TODO Auto-generated method stub
				TimeUnit.SECONDS.sleep(1);
				if(1==1) throw new IllegalArgumentException();
				return new Dto1("roy", 33, true);
			}
		});
		
		try {
			System.out.println(future.get());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test3() throws Exception {
		CountDownLatch latch = new CountDownLatch(5);
		ThreadPoolEx.getExecutor().submit(new TestThread(latch));
		ThreadPoolEx.getExecutor().submit(new TestThread(latch));
		ThreadPoolEx.getExecutor().submit(new TestThread(latch));
		ThreadPoolEx.getExecutor().submit(new TestThread(latch));
		ThreadPoolEx.getExecutor().submit(new TestThread(latch));
//		ThreadPoolEx.getExecutor().shutdown();
		latch.await();
		System.out.println(TestThread.i);
	}
	
	
	public static void main(String[] args) throws Exception {
//		test1();
//		test2();
		for(int i=1;i<=10;i++){
//			TestThread.i= new AtomicInteger(0);
			TestThread.i= 0;
			test3();
		}
		System.exit(1);
	}

}
