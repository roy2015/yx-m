package cn.zj.easynet.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.hssf.util.HSSFColor.VIOLET;


public class TestThread implements Runnable {
//	public static  AtomicInteger i= new AtomicInteger(0);
	public static volatile int i= 0;
	private CountDownLatch latch;
	
	public TestThread(CountDownLatch latch) {
		super();
		this.latch = latch;
	}

	@Override
	public void run() {
		for(int i=0; i<100; i++){
//			this.i.addAndGet(1);
			this.i = this.i +1;
		}
		latch.countDown();

	}

}
