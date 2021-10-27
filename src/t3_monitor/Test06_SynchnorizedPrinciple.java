package t3_monitor;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description Synchnorized原理-字节码
 * @date 2021/10/27 11:03 下午
 **/
public class Test06_SynchnorizedPrinciple {
    static final Object lock = new Object();
    static int counter = 0;

    public static void main(String[] args) {
        synchronized (lock) {
            counter++;
        }
    }
}
