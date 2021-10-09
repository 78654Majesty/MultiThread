package juc.start;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 如何去创建线程
 * 三种方式：1继承Tread接口 2实现Runnable接口 3线程池
 * @date 2021/10/9 3:10 下午
 **/
public class Test02_HowToCreateThread {
    /**
     * 1.继承Tread类
     */
    private static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Hello MyThread!");
        }
    }

    /**
     * 实现Runnable接口
     */
    private static class MyRun implements Runnable {
        @Override
        public void run() {
            System.out.println("Hello MyRun!");
        }
    }

    public static void main(String[] args) {
        new MyThread().start();
        new Thread(new MyRun()).start();
        // 匿名类部类实现 实际也只重写Runnable接口
        new Thread(() -> System.out.println("Hello MyRun Lambda!")).start();
    }
}
