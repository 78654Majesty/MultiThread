package t4_memory;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/16 10:42 下午
 **/
public final class Test04_StaticLazySingleton {
    private static class LazyHolder {
        public static final Test04_StaticLazySingleton instance = new Test04_StaticLazySingleton();
    }

    public static Test04_StaticLazySingleton getInstance() {
        return LazyHolder.instance;
    }
}
