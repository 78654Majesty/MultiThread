package t5_nolock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/21 9:42 下午
 **/
public class Test03_MyAtomicInteger {
    private volatile int value;
    private static final long VALUE_OFFSET;
    private static final Unsafe UNSAFE;

    static {
        UNSAFE = UnsafeAccessor.getUnsafe();
        try {
            VALUE_OFFSET = UNSAFE.objectFieldOffset(Test03_MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getValue() {
        return value;
    }

    public boolean compareAndSet(int afterValue) {
        return UNSAFE.compareAndSwapInt(this, VALUE_OFFSET, value, afterValue);
    }
}

class UnsafeAccessor {
    public static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
