package t7_concurrent.t1_pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/25 10:52 下午
 **/
@Slf4j
public class Test03_Submit {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<String> future = executor.submit(() -> {
            log.debug("running");
            Thread.sleep(1000);
            return "done";
        });
        log.debug("{}",future.get());
    }
}
