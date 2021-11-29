package t7_concurrent.t1_pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description schedule定时任务
 * 目标：
 * 实现每周18:00:00执行任务
 * @date 2021/11/28 11:59 下午
 **/
@Slf4j
public class Test04_ScheduleTask {
    private static final ThreadFactory NAME_FACTORY = new ThreadFactoryBuilder().setNameFormat("fang-test-%d").build();
    //    private static final ExecutorService pool = new ThreadPoolExecutor(5, 200,
//            0L, TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<>(1024), nameFactory, new ThreadPoolExecutor.AbortPolicy());
    private static final ScheduledExecutorService POOL = new ScheduledThreadPoolExecutor(2, NAME_FACTORY);

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        // 修改执行时间
        LocalDateTime targetTime = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);
        if (now.compareTo(targetTime) > 0) {
            // 如果当前时间>执行时间，向下推一周
            targetTime = targetTime.plusWeeks(1);
        }
        // 推迟时间
        long initialDelay = Duration.between(now, targetTime).toMillis();
        // 周期
        long period = 1000 * 60 * 60 * 24 * 7;
        POOL.scheduleAtFixedRate(() -> {
            log.debug("running。。。");
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }

//    public void execute(Runnable command, long timeout, TimeUnit unit) {
//        submittedCount.incrementAndGet();
//        try {
//            super.execute(command);
//        } catch (RejectedExecutionException rx) {
//            if (super.getQueue() instanceof TaskQueue) {
//                final TaskQueue queue = (TaskQueue) super.getQueue();
//                try {
//                    if (!queue.force(command, timeout, unit)) {
//                        submittedCount.decrementAndGet();
//                        throw new RejectedExecutionException("Queue capacity is full.");
//                    }
//                } catch (InterruptedException x) {
//                    submittedCount.decrementAndGet();
//                    Thread.interrupted();
//                    throw new RejectedExecutionException(x);
//                }
//            } else {
//                submittedCount.decrementAndGet();
//                throw rx;
//            }
//        }
//    }
}
