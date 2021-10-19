package juc.t2_thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/10/19 10:05 下午
 **/
@Slf4j
public class Test07_InterruptPark {
    public static void main(String[] args) throws InterruptedException {
        parkTest1();
//        parkTest2();
    }

    private static void parkTest1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.info("park...");
            LockSupport.park();
            log.info("unPark...");
            log.info("打断状态:{}", Thread.currentThread().isInterrupted());

            LockSupport.park();
            log.info("unPark...");
        }, "t1");
        t1.start();
        sleep(1);
        t1.interrupt();
    }

    private static void parkTest2() throws InterruptedException {
        Thread t2 = new Thread(() -> {
            log.info("park...");
            LockSupport.park();
            log.info("unPark...");
            log.info("打断状态:{}", Thread.interrupted());

            LockSupport.park();
            log.info("unPark...");
        }, "t2");
        t2.start();
        sleep(1);
        t2.interrupt();
    }
}
