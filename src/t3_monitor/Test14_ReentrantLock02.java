package t3_monitor;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/10 11:34 下午
 **/
public class Test14_ReentrantLock02 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        try {
        } finally {
            lock.unlock();
        }
    }

}
