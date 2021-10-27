package t3_monitor;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 轻量级锁
 * @date 2021/10/27 11:35 下午
 **/
public class Test07_LightLock {
    static final Object obj = new Object();

    public static void method1() {
        // 同步块 A
        synchronized (obj) {
            method2();
        }
    }

    public static void method2() {
        // 同步块 B
        synchronized (obj) {
        }
    }
}
