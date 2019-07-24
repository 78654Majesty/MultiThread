package deadlock;

import java.math.BigDecimal;

/**
 * description
 * 转账问题：A转给B B同时转给C 如何保证资金问题？
 * 通过银行管家的模式解决：
 * 锁1：锁转入账户，锁2：锁转出账户 导致死锁的问题
 * 产生死锁的问题：
 * 1、互斥性：同一时刻只能一个线程访问共享资源
 * 2、占有且等待：线程T1在获取A资源之后并且等待B资源的释放，并且不释放A资源
 * 3、不可抢占：其他线程不可抢占T1线程获取的资源
 * 4、循环等待：T1线程等待获取B资源，T2线程获得B资源等待A资源
 * <p>
 * 当前解决：2问题 占有且等待，通过一次性获取所有资源
 *
 * @author fanglingxiao
 * @date 2019/7/24
 */
public class Account {
    private static HouseKeeper houseKeeper = new HouseKeeper();
    private BigDecimal balance = new BigDecimal(1000);

    boolean transfer(Account target, BigDecimal amount) {
        long startTime = System.currentTimeMillis();
        while (!houseKeeper.apply(this, target)) {
            //如果没有获取到资源一直等待,设置等待时间为1分钟
            if (System.currentTimeMillis() - startTime >= 1000 * 60) {
                return false;
            }
        }
        try {
            //锁定转出账户
            synchronized (this) {
                //锁定转入账户
                synchronized (target) {
                    if (balance.compareTo(amount) < 0) {
                        return false;
                    }
                    this.balance = this.balance.subtract(amount);
                    target.balance = target.balance.add(amount);
                }
            }
        } finally {
            houseKeeper.free(this, target);
        }
        return true;
    }

    public static void main(String[] args) {
        Account account = new Account();
        Account target = new Account();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = account.transfer(target, new BigDecimal(100));
                System.out.println("-----------------------------");
                System.out.println(Thread.currentThread());
                System.out.println("转账结果：" + result);
                System.out.println("转出账户金额：" + account.balance);
                System.out.println("转入账户金额：" + target.balance);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = account.transfer(target, new BigDecimal(100));
                System.out.println("-----------------------------");
                System.out.println(Thread.currentThread());
                System.out.println("转账结果：" + result);
                System.out.println("转出账户金额：" + account.balance);
                System.out.println("转入账户金额：" + target.balance);
            }
        }).start();
    }
}

