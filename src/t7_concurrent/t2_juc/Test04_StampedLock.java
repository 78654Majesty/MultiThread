package t7_concurrent.t2_juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/12/5 12:04 上午
 **/
public class Test04_StampedLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        new Thread(() -> {
            dataContainer.read();
        }, "t1").start();

        Thread.sleep(1000);

        new Thread(() -> {
            dataContainer.write(100);
        }, "t2").start();
    }
}

@Slf4j
class DataContainerStamped {
    private int data;
    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data) {
        this.data = data;
    }

    public int read() {
        long readStamp = lock.tryOptimisticRead();
        if (lock.validate(readStamp)) {
            return data;
        }
        // 锁升级
        try {
            readStamp = lock.readLock();
            return data;
        } finally {
            lock.unlockRead(readStamp);
        }
    }

    public void write(int data) {
        long stamp = lock.writeLock();
        try {
            this.data = data;
        } finally {
            lock.unlockWrite(stamp);
        }

    }
}
