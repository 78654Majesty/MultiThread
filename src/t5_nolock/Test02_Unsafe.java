package t5_nolock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/21 9:29 下午
 **/
public class Test02_Unsafe {
    /**
     * Unsafe不能直接通过构造方法获取其对象
     * 只能通过反射获取
     *
     * @param args
     */
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        // 允许访问私有变量
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        System.out.println(unsafe);
    }
}
