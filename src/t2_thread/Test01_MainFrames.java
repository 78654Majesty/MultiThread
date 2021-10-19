package t2_thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/10/14 11:06 下午
 **/
@Slf4j
public class Test01_MainFrames {
    public static void main(String[] args) {
        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        log.info("{}",m);
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}
