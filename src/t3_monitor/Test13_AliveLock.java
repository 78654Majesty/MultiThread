package t3_monitor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/10 11:11 下午
 **/
@Slf4j
public class Test13_AliveLock {
    static int count = 10;
    static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
// 期望减到 0 退出循环
            while (count > 0) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count--;
                log.debug("count: {}", count);
            }
        }, "t1").start();
        new Thread(() -> {
// 期望超过 20 退出循环
            while (count < 20) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                log.debug("count: {}", count);
            }
        }, "t2").start();
    }
}
