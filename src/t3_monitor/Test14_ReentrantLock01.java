package t3_monitor;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/10 11:34 下午
 **/
public class Test14_ReentrantLock01 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        try {
            m1();
        } finally {
            lock.unlock();
        }
    }

    public static void m1() {
        lock.lock();
        try {
            m2();
        } finally {
            lock.unlock();
        }
    }

    public static void m2() {
        lock.lock();
        try {

        } finally {
            lock.unlock();
        }
    }
}
