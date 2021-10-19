package juc.t2_thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 打断正在运行的线程
 * @date 2021/10/19 9:09 下午
 **/
@Slf4j
public class Test05_Interrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            while (true){
                boolean interrupted = Thread.currentThread().isInterrupted();
                if (interrupted){
                    log.info("current thread has bean interrupt!");
                    break;
                }
            }
        },"t1");
        t1.start();
        Thread.sleep(1000);
        log.info("interrupt");
        t1.interrupt();
        log.info("是否被打断：{}",t1.isInterrupted());
    }
}
