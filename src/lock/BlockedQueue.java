package lock;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * description
 * 阻塞队列 模拟BlockingQueue
 * @author fanglingxiao
 * @date 2019/7/25
 */
public class BlockedQueue<E> {
    private int size;
    private final Lock lock = new ReentrantLock();
    /**条件A：队列不满*/
    private final Condition notFull = lock.newCondition();
    /**条件B：队列不空*/
    private final Condition notEmpty = lock.newCondition();

    private LinkedList<E> list = new LinkedList<>();

    public BlockedQueue(int size){
        this.size = size;
    }

    /**生产者：消息入队*/
    void enQueue(E t){
        lock.lock();
        try{
            while(list.size() == size){
                System.out.println("队列满，等待消费");
                notFull.await();
            }
            //执行具体逻辑
            list.add(t);
            //通知消费者可入队
            notEmpty.signal();
            System.out.println("入队成功，通知消费者");
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    void deQueue(E t){
        lock.lock();
        try{
            while (list.isEmpty()){
                System.out.println("队列空，等待生产者");
                notEmpty.wait();
            }
            //执行具体逻辑
            list.removeFirst();
            //通知生产者可入队
            notFull.signal();
            System.out.println("消费成功！通知生产者");
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

class TestBlockedQueue {
    public static void main(String[] args) {
        BlockedQueue<Integer> queue = new BlockedQueue<>(1);
        new Thread(() -> {
            queue.enQueue(1);
            queue.enQueue(2);
        }).start();
        new Thread(() -> queue.deQueue(2)).start();
    }
}
