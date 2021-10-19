package juc.t2_thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 统筹烧开水案例
 * @date 2021/10/19 11:22 下午
 **/
@Slf4j
public class Test09_BoilWaterTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = washKettle(1);
        Thread t2 = boilWater(15, t1);
        Thread t3 = operateOthers(3, t1);
        t1.start();
        t2.start();
        t3.start();
        t2.join();
        log.info("泡茶");
    }

    private static Thread operateOthers(int min, Thread t) {
        return new Thread(() -> {
            try {
                t.join();
                log.info("洗茶壶、洗茶杯、拿茶叶耗时：" + min + "分钟");
                Thread.sleep(min * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "operateOthers");
    }

    private static Thread boilWater(int min, Thread t) {
        return new Thread(() -> {
            try {
                t.join();
                log.info("烧开水耗时：" + min + "分钟");
                Thread.sleep(min * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "boilWater");
    }

    private static Thread washKettle(int min) {
        return new Thread(() -> {
            log.info("洗茶壶耗时：" + min + "分钟");
            try {
                Thread.sleep(min * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "washKettle");
    }
}
