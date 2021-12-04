package t7_concurrent.t2_juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/29 9:54 下午
 **/
@Slf4j
public class Test01_AQS {
    private static final MyLock LOCK = new MyLock();
    public static void main(String[] args) {
        new Thread(()->{
            LOCK.lock();
            try {
                log.debug("locking...");
                Thread.sleep(9000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.debug("unLock...");
                LOCK.unlock();
            }
        },"t1").start();

        new Thread(()->{
            LOCK.lock();
            try {
                log.debug("locking...");
            }finally {
                log.debug("unLock...");
                LOCK.unlock();
            }
        },"t2").start();

        new Thread(()->{
            LOCK.lock();
            try {
                log.debug("locking...");
            }finally {
                log.debug("unLock...");
                LOCK.unlock();
            }
        },"t3").start();
    }
}

class MyLock implements Lock {

    /**
     * 独占锁同步器
     */
    class MyAQS extends AbstractQueuedSynchronizer {

        /**
         * @param arg 可重入锁参数
         * @return boolean
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                // 类似Monitor中set持有锁对象
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * @param arg 可重入锁参数
         * @return boolean
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                return true;
            }
            // 由于state是volatile，保证原子可见性，防止指令重排，所以setState在后
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        /**
         * 判断当前是否持有锁
         *
         * @return boolean
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        protected Condition getCondition() {
            return new ConditionObject();
        }
    }

    private final MyAQS aqs = new MyAQS();

    @Override
    public void lock() {
        // 尝试获取锁，不成功则加入阻塞队列
        aqs.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        aqs.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return aqs.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return aqs.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        aqs.release(1);
    }

    @Override
    public Condition newCondition() {
        return aqs.getCondition();
    }
}
