package t4_memory;

import java.io.Serializable;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 类上加final是因为避免该类被继承
 * @date 2021/11/16 10:04 下午
 **/
public final class Test03_StaticSingleton implements Serializable {
    private static final Test03_StaticSingleton instance = new Test03_StaticSingleton();

    public static Test03_StaticSingleton getInstance() {
        return instance;
    }

    /**
     * 防止在反序列化时破坏单例
     * 反序列化在创建对象时，识别到readResolve方法时会放回当前该方法的返回值，就不会在创建新的对象
     */
    public Object readResolve() {
        return instance;
    }
}
