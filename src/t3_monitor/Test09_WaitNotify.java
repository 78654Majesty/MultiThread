package t3_monitor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/2 10:27 下午
 **/
@Slf4j
public class Test09_WaitNotify {
    private static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (obj) {
                try {
                    log.info("t1 准备执行wait()j进入等待");
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("t1 被唤醒开始执行");
            }
        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            synchronized (obj) {
                try {
                    log.info("t2 准备执行wait()j进入等待");
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("t2 被唤醒开始执行");
            }
        }, "t2");
        t2.start();

        Thread.sleep(3000);
        synchronized (obj){
            log.info("main 准备唤醒obj上其他的线程");
//            obj.notify();
            obj.notifyAll();
        }
    }
}
