package juc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class ReentrantLockDemo {

    static ReentrantLock producer = new ReentrantLock();
    static ReentrantLock consume = new ReentrantLock();

    static Condition producerCondition = producer.newCondition();
    static Condition consumeCondition = consume.newCondition();

    static LinkedList<Integer> msgList = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        int size = 5;
        Random random = new Random();
        Thread producerThread = new Thread(()->{
            producer.lock();
            try{
                while (msgList.size() == 5){
                    producerCondition.await();
                    System.out.println("生产队列满 等待中。。。");
                }
                int i = random.nextInt(5);
                System.out.println("生产加入队列-"+i);
                msgList.add(i);
//                ConcurrentHashMap;
//                Supplier;
//                HashMap；
//                Executors.newFixedThreadPool();
                consumeCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                producer.unlock();
            }
        });

        Thread consumerThread = new Thread(()->{
            consume.lock();
            try {
                while (msgList.isEmpty()){
                    System.out.println("消费队列为空 等待中。。。");
                    consumeCondition.await();
                }
                System.out.println("消费者消费-"+msgList.getFirst());
                msgList.removeFirst();
                producerCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                consume.unlock();
            }

        });

        producerThread.start();
        Thread.sleep(1000);
        consumerThread.start();
    }
}
