package t7_concurrent.t1_pool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 自定义线程池(阻塞队列 、 拒绝策略)
 * @date 2021/11/22 10:30 下午
 **/
@Slf4j
public class Test01_MyThreadPool {
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(10,
                2,
                TimeUnit.MILLISECONDS,
                1000,
                ((queue, task) -> {
                    // 1.死等
//                    queue.put(task);
                    // 2.超时等待
                    queue.tryPut(task, 10, TimeUnit.MILLISECONDS);
                    // 3.让调用者放弃执行
//                    log.debug("放弃执行");
                    // 4.让调用者抛出异常
//                    throw new RuntimeException("队列满");
                    // 5.让调用者自己执行
//                    task.run();
                })
        );
        for (int i = 0; i < 15; i++) {
            int j = i;
            pool.execute(() -> {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.debug("{}", j);
            });
        }
    }
}

@Slf4j
class ThreadPool {
    /**
     * blockQueue 任务队列
     * coreSize 核心线程数大小
     * timeUnit 线程超时单位
     * timeout 线程超时时间
     * works 线程集合
     */
    private final BlockQueue<Runnable> blockQueue;
    private final int coreSize;
    private final TimeUnit timeUnit;
    private final long timeout;
    private final HashSet<Worker> works = new HashSet<>();
    private final RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int blockQueueCapacity, int coreSize, TimeUnit timeUnit, long timeout, RejectPolicy<Runnable> rejectPolicy) {
        this.blockQueue = new BlockQueue<>(blockQueueCapacity);
        this.coreSize = coreSize;
        this.timeUnit = timeUnit;
        this.timeout = timeout;
        this.rejectPolicy = rejectPolicy;
    }

    /**
     * 1)当工作线程 < coreSize 创建工作线程执行
     * 2)当工作线程 >= coreSize 加入阻塞队列
     */
    public void execute(Runnable task) {
        synchronized (works) {
            if (works.size() < coreSize) {
                log.debug("新增worker");
                Worker worker = new Worker(task);
                works.add(worker);
                worker.start();
                return;
            }
            blockQueue.putPolicy(rejectPolicy, task);
        }
    }

    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        /**
         * 1)当task不为空时直接执行
         * 2)当task执行完从等待队列中获取task执行
         */
        @Override
        public void run() {
            while (task != null || (task = blockQueue.tryTake(timeout, timeUnit)) != null) {
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.getStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (works) {
                log.debug("移除worker{}", this);
                works.remove(this);
            }
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }
}

@FunctionalInterface
interface RejectPolicy<T> {
    /**
     * 拒绝策略方法
     *
     * @param queue 阻塞队列
     * @param task  任务
     */
    void reject(BlockQueue<T> queue, T task);
}

@Slf4j
class BlockQueue<T> {
    /**
     * queue 任务队列
     * lock 锁
     * provider 生产条件
     * consumer 消费条件
     * capacity 阻塞队列大小
     */
    private final Deque<T> queue = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition provider = lock.newCondition();
    private final Condition consumer = lock.newCondition();
    private final int capacity;

    public BlockQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 阻塞消费带超时时间
     */
    public T tryTake(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            // 将单位时间同义转换为纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    if (nanos <= 0) {
                        return null;
                    }
                    // 返回剩余等待时间
                    nanos = consumer.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            provider.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞消费
     */
    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    consumer.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            provider.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 生产策略
     */
    public void putPolicy(RejectPolicy<T> policy, T task) {
        lock.lock();
        try {
            if (queue.size() >= capacity) {
                policy.reject(this, task);
                return;
            }
            log.debug("将task加入阻塞队列");
            queue.addLast(task);
            consumer.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞生产带超时时间
     */
    public boolean tryPut(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() >= capacity) {
                try {
                    if (nanos <= 0) {
                        log.debug("等待____加入阻塞队列___timeout");
                        return false;
                    }
                    log.debug("等待____加入阻塞队列___ing");
                    nanos = provider.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("将task加入阻塞队列");
            queue.addLast(task);
            consumer.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞生产
     */
    public void put(T element) {
        lock.lock();
        try {
            while (queue.size() >= capacity) {
                try {
                    log.debug("等待____加入阻塞队列");
                    provider.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("将task加入阻塞队列");
            queue.addLast(element);
            consumer.signal();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}
