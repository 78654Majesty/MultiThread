package t5_nolock;

import sun.misc.Unsafe;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/21 6:48 下午
 **/
public class Test01_LongAdder {
    public static void main(String[] args) {
        new LongAdder();
//        Unsafe.getUnsafe().compareAndSwapLong();
    }
}
