package juc.t1_start;

import java.util.concurrent.TimeUnit;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 什么是线程
 * @date 2021/10/9 3:06 下午
 **/
public class Test01_WhatsThread {
    private static class T1 extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try{
                    TimeUnit.MICROSECONDS.sleep(1);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("T1");
            }
        }
    }

    public static void main(String[] args) {
        new T1().run();
        new T1().start();
        for (int i = 0; i < 10; i++) {
            try{
                TimeUnit.MICROSECONDS.sleep(1);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("main");
        }
    }
}
