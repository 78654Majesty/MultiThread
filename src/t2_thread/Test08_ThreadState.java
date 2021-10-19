package t2_thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 线程6种状态
 * @date 2021/10/19 10:49 下午
 **/
@Slf4j
public class Test08_ThreadState {
    public static void main(String[] args) {
        // running
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
//                log.info("Running...");
            }
        };
//        t1.start();
        // runnable
        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while (true) {
                    // runnable
                }
            }
        };
        t2.start();

        // Teminated(因为执行完后主线程sleep后打印state则已执行完)
        Thread t3 = new Thread("t3") {
            @Override
            public void run() {
//                log.info("Running...");
            }
        };
        t3.start();

        // timed_waiting
        Thread t4 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (Test08_ThreadState.class) {
                    try {
//                        log.info("timed_waiting");
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t4.start();

        Thread t5 = new Thread("t5") {
            @Override
            public void run() {
                try {
//                    log.info("Waiting...");
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t5.start();

        Thread t6 = new Thread("t6") {
            @Override
            public void run() {
//                log.info("blocked...");
                synchronized (Test08_ThreadState.class) {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t6.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("t1状态：{}", t1.getState());
        log.info("t2状态：{}", t2.getState());
        log.info("t3状态：{}", t3.getState());
        log.info("t4状态：{}", t4.getState());
        log.info("t5状态：{}", t5.getState());
        log.info("t6状态：{}", t6.getState());
    }
}
