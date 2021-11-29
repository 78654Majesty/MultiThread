package t7_concurrent.t1_pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 1-100累加
 * @date 2021/11/29 12:54 上午
 **/
@Slf4j
public class Test05_ForkJoin {
    private static final ForkJoinPool POOL = new ForkJoinPool(4);

    public static void main(String[] args) {
        Integer invoke = POOL.invoke(new MyTask(1, 100, POOL.getParallelism()));
        log.debug("result={}", invoke);
    }
}

@Slf4j
class MyTask extends RecursiveTask<Integer> {
    private final int poolSize;
    private final int begin;
    private final int end;
    private int sum;

    public MyTask(int begin, int end, int poolSize) {
        this.begin = begin;
        this.end = end;
        this.poolSize = poolSize;
        this.sum = end;
        log.debug("Initial MyTask begin={},end={},poolSize={} to calc result", begin, end, poolSize);
    }

    @Override
    protected Integer compute() {
        if (begin == end) {
            return begin;
        }
        if ((end - begin) <= poolSize) {
            MyTask task = new MyTask(begin, end - 1, poolSize);
            task.fork();
            int result = sum + task.join();
            log.debug("current result = {}", result);
            return result;
        }
        int avg = (begin + end) / poolSize;
        int result = 0;
        int start = begin;
        int end = avg;
//        for (int i = 0; i < poolSize; i++) {
//            MyTask task = new MyTask(start, end, 1);
//            task.fork();
//            result = sum + task.join();
//            start = end + 1;
//            if (i + 2 == poolSize) {
//                end = this.end;
//                continue;
//            }
//            end = end + avg;
//        }
        return result;
    }
}
