package deadlock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * description
 * 阻塞队列
 * @author fanglingxiao
 * @date 2019/7/25
 */
public class BlockedQueue<T> {

    private final Lock lock = new ReentrantLock();
    //条件A：队列不满
    private final Condition notFull = lock.newCondition();
    //条件B：队列不空
    private final Condition notEmpty = lock.newCondition();
    private final ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(16);
    //生产者：消息入队
    void enQueue(T t){
        lock.lock();
        try{
            //表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,
            //　　　　则返回true,否则返回false.（本方法不阻塞当前执行方法的线程）
            //条件：队列不满
            while(!blockingQueue.offer(t)){
                notFull.await();
            }
            //执行具体逻辑
            //通知消费者可入队
            notEmpty.signal();
        }catch (InterruptedException e){
            e.fillInStackTrace();
        }finally {
            lock.unlock();
        }
    }

    void deQueue(T t){
        lock.lock();
        try{
            while (blockingQueue.isEmpty()){
                notEmpty.wait();
            }
            //执行具体逻辑
            //通知生产者可入队
        }catch (InterruptedException e){
            e.fillInStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
