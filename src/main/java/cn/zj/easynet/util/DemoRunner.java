package cn.zj.easynet.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DemoRunner implements Runnable {

    private ReentrantLock lock;

    public DemoRunner(ReentrantLock lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.lock(); // block until condition holds
        try {
            TimeUnit.SECONDS.sleep(2);
            throw new IllegalArgumentException();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalArgumentException();
        } finally {
            lock.unlock();
        }
    }
}
