package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * description
 * Lock的lockInterruptibly()和interrupt()方法的使用
 * 使用lockInterruptibly()方法获取锁会标记一个可终止状态，
 * 使用当前线程调用interrupt()方法唤醒会触发InterruptException
 * 所以代码结果thread1-正常执行
 *
 * @author fanglingxiao
 * @date 2019/7/31
 */
public class ReentrantLockDemo {
    private static Lock lock1 = new ReentrantLock();
    private static Lock lock2 = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        //该线程先获取锁1,再获取锁2
        Thread thread = new Thread(new ThreadDemo(lock1, lock2));
        //该线程先获取锁2,再获取锁1
        Thread thread1 = new Thread(new ThreadDemo(lock2, lock1));
        thread.start();
        thread1.start();
        //是第一个线程中断
        TimeUnit.MILLISECONDS.sleep(10000);
        thread.interrupt();
    }

    static class ThreadDemo implements Runnable {
        Lock firstLock;
        Lock secondLock;

        public ThreadDemo(Lock firstLock, Lock secondLock) {
            this.firstLock = firstLock;
            this.secondLock = secondLock;
        }

        @Override
        public void run() {
            try {
                System.out.println("-------------");
                firstLock.lockInterruptibly();
                //更好的触发死锁
                TimeUnit.MILLISECONDS.sleep(100);
                secondLock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                firstLock.unlock();
                secondLock.unlock();
                System.out.println(Thread.currentThread().getName() + "正常结束!");
            }
        }
    }

}
