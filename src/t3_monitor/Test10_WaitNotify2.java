package t3_monitor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/2 11:02 下午
 **/
@Slf4j
public class Test10_WaitNotify2 {
    private static final Object lock = new Object();
    private static boolean hasWater = false;
    private static boolean hasTakeout = false;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                while (!hasWater) {
                    log.info("有水么？{},继续等待", hasWater);
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("水到了，开始干活");
            }
        }, "小李等水").start();
        new Thread(() -> {
            synchronized (lock) {
                while (!hasTakeout) {
                    log.info("饿了，外卖到了么？{},继续等待", hasWater);
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("外卖到了，开吃");
            }
        }, "小王等外卖").start();
        new Thread(() -> {
            synchronized (lock) {
                log.info("您的外卖到了");
                hasTakeout = true;
                lock.notifyAll();
            }
        }, "送外卖").start();
        new Thread(() -> {
            synchronized (lock) {
                log.info("您的水到了");
                hasWater = true;
                lock.notifyAll();
            }
        }, "送水").start();
    }
}
