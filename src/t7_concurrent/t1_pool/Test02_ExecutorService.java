package t7_concurrent.t1_pool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/24 10:36 下午
 **/
public class Test02_ExecutorService {
    private static final ThreadFactory factory = r -> new Thread(r,"flx");

    public static void main(String[] args) {
        Executor executor1 = Executors.newFixedThreadPool(1,factory);
        Executor executor2 = Executors.newCachedThreadPool();
        Executor executor3 = Executors.newSingleThreadExecutor(factory);
        ScheduledExecutorService executor4 = Executors.newScheduledThreadPool(2, factory);
//        executor4.scheduleWithFixedDelay()
    }
}
