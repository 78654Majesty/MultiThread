package juc.start;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 线程状态
 * @date 2021/10/9 3:39 下午
 **/
public class Test04_ThreadState {
    private static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("状态：" + this.getState());
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println(i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread t1 = new MyThread();
        System.out.println("状态：" + t1.getState());
        t1.start();
        t1.join();
        System.out.println("状态：" + t1.getState());
    }
}
