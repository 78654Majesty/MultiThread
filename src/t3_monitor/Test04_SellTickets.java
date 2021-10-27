package t3_monitor;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 卖票
 * @date 2021/10/27 9:35 下午
 **/
@Slf4j
public class Test04_SellTickets {
    public static void main(String[] args) {
        TicketWindow ticketWindow = new TicketWindow(1000);
        List<Thread> list = new ArrayList<>();
        // 用来存储卖出去多少张票
        List<Integer> sellCount = new Vector<>();
        for (int i = 0; i < 500; i++) {
            Thread t = new Thread(() -> {
                // 分析这里的竞态条件
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int count = ticketWindow.sell(randomAmount());
                sellCount.add(count);
            });
            list.add(t);
            t.start();
        }
        list.forEach((t) -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 买出去的票求和
        log.debug("卖出数量:{}", sellCount.stream().mapToInt(c -> c).sum());
        log.debug("剩余数量:{}", ticketWindow.getCount());
    }

    // Random 为线程安全
    static Random random = new Random();

    public static int randomAmount() {
        return random.nextInt(5) + 1;
    }
}

class TicketWindow {
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public synchronized int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}
