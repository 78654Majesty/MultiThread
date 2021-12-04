package t7_concurrent.t2_juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/12/4 8:24 下午
 **/
@Slf4j
public class Test03_ReentrantReadWriteLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainer dataContainer = new DataContainer();
//        readRead(dataContainer);
//        readWrite(dataContainer);
        writeWrite(dataContainer);
    }

    private static void writeWrite(DataContainer dataContainer) throws InterruptedException {
        new Thread(() -> {
            dataContainer.write(2);
        }, "t1").start();

        new Thread(() -> {
            dataContainer.write(3);
        }, "t2").start();
        Thread.sleep(1000);
        log.info("{}",dataContainer.toString());
    }

    private static void readWrite(DataContainer dataContainer) {
        new Thread(() -> {
            Object read = dataContainer.read();
            log.info("读取{}",read);
        }, "t1").start();

        new Thread(() -> {
            dataContainer.write(2);
        }, "t2").start();
    }

    private static void readRead(DataContainer dataContainer) {
        new Thread(() -> dataContainer.read(), "t1").start();

        new Thread(() -> dataContainer.read(), "t2").start();
    }
}

@Slf4j
class DataContainer {
    private Object data;
    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private final ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public DataContainer() {
        data = 1;
    }

    public Object read() {
        log.info("获取读锁...");
        r.lock();
        try {
            log.info("读ing...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return data;
        } finally {
            log.info("释放读锁...");
            r.unlock();
        }
    }

    public void write(Object data) {
        log.info("获取写锁...");
        w.lock();
        try {
            log.info("写ing...");
            this.data = data;
        } finally {
            log.info("释放写锁...");
            w.unlock();
        }
    }

    @Override
    public String toString() {
        return "DataContainer{" +
                "data=" + data +
                '}';
    }
}