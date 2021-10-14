package juc.start;


import lombok.extern.slf4j.Slf4j;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 如何去创建线程
 * 三种方式：1继承Tread类 2实现Runnable接口 3线程池
 * @date 2021/10/9 3:10 下午
 **/
@Slf4j
public class Test02_HowToCreateThread {
    /**
     * 1.继承Tread类
     */
    private static class MyThread extends Thread {
        @Override
        public void run() {
            log.info("Extends Thread");
        }
    }

    /**
     * 实现Runnable接口
     */
    private static class MyRun implements Runnable {
        @Override
        public void run() {
            log.info("Hello MyRun!");
        }
    }

    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.setName("Extends Thread");
        t.start();
        new Thread(new MyRun(),"Runnable").start();
        // 匿名类部类实现 实际也只重写Runnable接口
        new Thread(() -> log.info("Hello MyRun Lambda!"),"Lambda").start();
    }
}
