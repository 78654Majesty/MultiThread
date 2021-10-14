package juc.t2_thread;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/10/14 11:34 下午
 **/
public class Test02_MultiThreadFrames {
    public static void main(String[] args) {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                method1(20);
            }
        };
        t1.setName("t1");
        t1.start();
        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        System.out.println(m);
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}
