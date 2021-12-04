package t7_concurrent.t2_juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/12/5 12:16 上午
 **/
@Slf4j
public class Test05_Semaphore {
    private static final Semaphore semaphore = new Semaphore(3, false);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            useSemaphore(i);
//            noUseSemaphore(i);
        }
    }

    private static void noUseSemaphore(int i) {
        new Thread(() -> {
            try {
                log.info("running...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("end...");
        }, "t" + (i + 1)).start();
    }

    private static void useSemaphore(int i) {
        new Thread(() -> {
            try {
                semaphore.acquire();
                log.info("running...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
            log.info("end...");
        }, "t" + (i + 1)).start();
    }
}
