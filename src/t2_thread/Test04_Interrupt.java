package t2_thread;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/10/19 9:04 下午
 **/
@Slf4j
public class Test04_Interrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            try {
                sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1");
        t1.start();

        sleep(1);
        Thread.sleep(1000);
        log.info("打断:interrupt");
        t1.interrupt();
        log.info("打断状态:{}",t1.isInterrupted());
    }
}
