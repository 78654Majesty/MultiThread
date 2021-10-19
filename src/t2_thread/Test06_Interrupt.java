package t2_thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 两阶段终止模式
 * @date 2021/10/19 9:20 下午
 **/
@Slf4j
public class Test06_Interrupt {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        Thread.sleep(3500);
        tpt.stop();
    }
}

@Slf4j
class TwoPhaseTermination {
    private Thread monitor;

    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    log.info("后续处理");
                    break;
                }
                try {
                    // 情况1 抛异常
                    Thread.sleep(1000);
                    // 情况2 正常执行
                    log.info("执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 在睡眠中被打断，抛出异常，并且将打断标识置为false
                    // 所以需要重新打断，下次循环进入时，判断当前状态并break
                    current.interrupt();
                }
            }
        }, "start");
        monitor.start();
    }

    public void stop() {
        if (Objects.isNull(monitor)) {
            return;
        }
        monitor.interrupt();
    }
}
