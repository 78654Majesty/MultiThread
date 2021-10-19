[TOC]  
## 1.线程与进程
### 1.1进程  
程序由指令和数据组成，这些指令要运行、数据要加载，就必须将指令加载至cpu、数据加载进内存。  
指令运行的过程中依赖于磁盘、网络等设备，进程就是用来管理指令、内存、IO的。  

### 1.2线程  
什么是线程？
1. 一个进程可以有多个线程  
2. 一个线程就是一个指令流，将这些指令按顺序交给cpu来执行  
3. Java中，线程是最小的调度单位，进程是最小的资源分配单位  

## 2.并行与并发  
### 2.1 并行  
![image.png](https://note.youdao.com/yws/res/c/WEBRESOURCE42b84af5d7817bdce597f9ab48646a1c)  
多核cpu同时执行不同的线程相互没有影响  
### 2.2 并发  
![image.png](https://note.youdao.com/yws/res/3/WEBRESOURCEada850afdf02698fa93db52051236233)  
cpu其中一个核心线程切换(时间很短)线程来执行某个程序，根据时间片来切换执行  

## 3.应用  
### 3.1异步调用  
从方法调用的角度来说  
- 需要等待结果返回，才能继续运行就是**同步**  
- 不需要等待结果返回，就能继续运行就是**异步**  
**注意** ：同步再多线程中还有另外一层意思，是让多个线程步调一致  

**结论**：  
- 如果在项目中，视频文件需要转换格式等操作比较费时，这时开一个新线程处理，避免阻塞主线程  
- tomcat的异步servlet也是类似的目的，让用户线程处理耗时比较长的操作，避免阻塞tomcat的工作线程  
- ui程序中，开线程进行其他的操作，避免阻塞ui线程  

### 3.2提高效率  
**总结**  
1. 单核 cpu 下，多线程不能实际提高程序运行效率，只是为了能够在不同的任务之间切换，不同线程轮流使用 cpu ，不至于一个线程总占用 cpu，别的线程没法干活
2. 多核 cpu 可以并行跑多个线程，但能否提高程序运行效率还是要分情况的
     有些任务，经过精心设计，将任务拆分，并行执行，当然可以提高程序的运行效率。但不是所有计算任
     务都能拆分(参考后文的【阿姆达尔定律】)
     也不是所有任务都需要拆分，任务的目的如果不同，谈拆分和效率没啥意义
3. IO 操作不占用 cpu，只是我们一般拷贝文件使用的是【阻塞 IO】，这时相当于线程虽然不用 cpu，但需要一 直等待 IO 结束，没能充分利用线程。所以才有后面的【非阻塞 IO】和【异步 IO】优化  

[TOC]
## 1.如何创建线程  
1. 继承Thread类  
2. 实现Runnable接口  
3. 线程池创建  
```
/**
 * @author fanglingxiao
 * @version 1.0
 * @description 如何去创建线程
 * 三种方式：1继承Tread类 2实现Runnable接口 3线程池
 * @date 2021/10/9 3:10 下午
 **/
@Slf4j
public class Test02_HowToCreateThread {
    /**
     * 1.继承Tread类
     */
    private static class MyThread extends Thread {
        @Override
        public void run() {
            log.info("Extends Thread");
        }
    }

    /**
     * 实现Runnable接口
     */
    private static class MyRun implements Runnable {
        @Override
        public void run() {
            log.info("Hello MyRun!");
        }
    }

    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.setName("Extends Thread");
        t.start();
        new Thread(new MyRun(),"Runnable").start();
        // 匿名类部类实现 实际也只重写Runnable接口
        new Thread(() -> log.info("Hello MyRun Lambda!"),"Lambda").start();
    }
}
```

## 2.原理  
根据实现Runnable接口看Thread的源码  
![image.png](https://note.youdao.com/yws/res/2/WEBRESOURCEdf246097d67cadfd556af26bc5901f22)  
![image.png](https://note.youdao.com/yws/res/0/WEBRESOURCE699b246e858038a82b44ab81d327c400)  
![image.png](https://note.youdao.com/yws/res/8/WEBRESOURCEb6466fe3fbad282a7cdcf559ffe0f5b8)  
![image.png](https://note.youdao.com/yws/res/8/WEBRESOURCE52c6857cfa8a33f577a4afc326006c28)  

## 3.查看进程的方法  
### 3.1 windows  
- 任务管理器可以直接查看  
- tasklist 查看进程  
    tasklist ｜ findstr xxx
- taskkill 杀死进程  
    taskkill /F /PID pid 杀死进程  

### 3.2 linux  
- ps -ef |grep xxx
- kill  
- top  
    top -H -p pid 表示查看某个进程下各个线程的信息  


### 3.3 Java
- jps  
- jstack  
    jstack pid 表示查看当前时间点的进程下的线程信息  
- jconsole  
    1. 终端输入jconsole命令  
    2. 按照如下方式运行Java类  
    ```
     java -Djava.rmi.server.hostname=`ip地址` 
     -Dcom.sun.management.jmxremote 
     -Dcom.sun.management.jmxremote.port=`连接端口`
     -Dcom.sun.management.jmxremote.ssl=是否安全连接 
     -Dcom.sun.management.jmxremote.authenticate=是否认证 java类
     ```
     3. 如果要认证访问，还需要做如下步骤  
        复制 jmxremote.password 文件  
        修改 jmxremote.password 和 jmxremote.access 文件的权限为 600 即文件所有者可读写   
        连接时填入 controlRole(用户名)，R&D(密码)

## 4.线程运行原理  
### 4.1 栈与栈帧  
Java Virtual Machine Stacks (Java 虚拟机栈)  
我们都知道 JVM 中由堆、栈、方法区所组成，其中栈内存是给谁用的呢?其实就是线程，每个线程启动后，虚拟 机就会为其分配一块栈内存。  
- 每个栈由多个栈帧(Frame)组成，对应着每次方法调用时所占用的内存  
- 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法  
```
@Slf4j
public class Test01_Frames {
    public static void main(String[] args) {
        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        log.info("{}",m);
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}
```
执行原理图解  
![image.png](https://note.youdao.com/yws/res/a/WEBRESOURCE6f4a3ca84356599e257a0537fcfadd2a)  

### 4.2 线程上下文切换  
因为以下一些原因导致 cpu 不再执行当前的线程，转而执行另一个线程的代码  
- 线程的 cpu 时间片用完  
- 垃圾回收  
- 有更高优先级的线程需要运行  
- 线程自己调用了 sleep、yield、wait、join、park、synchronized、lock 等方法  

当 Context Switch 发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，Java 中对应的概念 就是程序计数器(Program Counter Register)，它的作用是记住下一条 jvm 指令的执行地址，是线程私有的  
- 状态包括程序计数器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、实例数据、返回地址   
- Context Switch 频繁发生会影响性能  

## 5.常用方法  
![image.png](https://note.youdao.com/yws/res/4/WEBRESOURCEf13077c2a5ff5b07d0f9475075f67994)  
![image.png](https://note.youdao.com/yws/res/a/WEBRESOURCEcbc84e147e36792ef9a23e925414c96a)  

### 5.1 start&run  
- 直接调用 run 是在主线程中执行了 run，没有启动新的线程  
- 使用 start 是启动新的线程，通过新的线程间接执行 run 中的代码  


### 5.2 sleep&yield&join
**sleep**  
1. 调用 sleep 会让当前线程从 Running 进入 Timed Waiting 状态(阻塞)  
2. 其它线程可以使用 interrupt 方法打断正在睡眠的线程，这时 sleep 方法会抛出 InterruptedException   
3. 睡眠结束后的线程未必会立刻得到执行  
4. 建议用 TimeUnit 的 sleep 代替 Thread 的 sleep 来获得更好的可读性   
TimeUnit.SECONDS.sleep(1);

**yield**  
1. 调用 yield 会让当前线程从 Running 进入 Runnable 就绪状态，然后调度执行其它线程（不一定其他线程能抢夺过这个线程资源）  
2. 具体的实现依赖于操作系统的任务调度器  

**线程优先级**    
Thread.setPriority(1-10)
1. 线程优先级会提示(hint)调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它   
2. 如果 cpu 比较忙，那么优先级高的线程会获得更多的时间片，但 cpu 闲时，优先级几乎没作用  

**join**  
等待另一个线程执行完  

**test代码**   
```java
public class Test03_Sleep_Yield_Join {
    public static void main(String[] args) {
//        testSleep();
//        testYield();
        testJoin();
    }

    /**
     * sleep
     * 休眠让其他线程执行
     */
    private static void testSleep() {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("sleep:" + i);
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * yield
     * 让出一下cpu来让其他线程执行，至于其他线程能否抢占到线程未知
     */
    private static void testYield() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("Yield A:" + i);
                if (i % 10 == 0) {
                    Thread.yield();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("Yield B:" + i);
                if (i % 10 == 0) {
                    Thread.yield();
                }
            }
        }).start();
    }

    /**
     * join
     * 等待另一个线程执行完
     */
    private static void testJoin() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("Join A:" + i);
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("Join B:" + i);
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
    }
}
```
### 5.3 interrupt  
打断阻塞状态的线程  
比如：sleep,wait,join的线程,打断这些阻塞的状态会InterruptedException异常
#### 5.3.1 打断正在运行的线程
```java
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
```

#### 5.3.2 两阶段终止模式  
![image.png](https://note.youdao.com/yws/res/2/WEBRESOURCEc18dce0f6d3b1d05ca9b753f6b2fd962)  
在正常执行流程中被打断不影响打断状态，当在sleep、wait、join的操作是被打断  
则会抛出InterruptedException异常，并且会将打断状态置为false。  
所以在catch中需要重新interrupt当前线程  
```java
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
                    // 情况1
                    Thread.sleep(1000);
                    // 情况2
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
```
执行结果：
```
21:53:03.589 [start] INFO juc.t2_thread.TwoPhaseTermination - 执行监控记录
21:53:04.596 [start] INFO juc.t2_thread.TwoPhaseTermination - 执行监控记录
21:53:05.600 [start] INFO juc.t2_thread.TwoPhaseTermination - 执行监控记录
java.lang.InterruptedException: sleep interrupted
	at java.lang.Thread.sleep(Native Method)
	at juc.t2_thread.TwoPhaseTermination.lambda$start$0(Test06_Interrupt.java:37)
	at java.lang.Thread.run(Thread.java:748)
21:53:06.088 [start] INFO juc.t2_thread.TwoPhaseTermination - 后续处理
```

### 5.3.4 park  
```java
/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/10/19 10:05 下午
 **/
@Slf4j
public class Test07_InterruptPark {
    public static void main(String[] args) throws InterruptedException {
        parkTest1();
//        parkTest2();
    }

    private static void parkTest1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.info("park...");
            LockSupport.park();
            log.info("unPark...");
            log.info("打断状态:{}", Thread.currentThread().isInterrupted());
            
            LockSupport.park();
            log.info("unPark...");
        }, "t1");
        t1.start();
        Thread.sleep(1000);
        t1.interrupt();
    }

    private static void parkTest2() throws InterruptedException {
        Thread t2 = new Thread(() -> {
            log.info("park...");
            LockSupport.park();
            log.info("unPark...");
            log.info("打断状态:{}", Thread.interrupted());

            LockSupport.park();
            log.info("unPark...");
        }, "t2");
        t2.start();
        Thread.sleep(1000);
        t2.interrupt();
    }
}
```
parkTest1()结果
```
22:16:59.178 [t1] INFO juc.t2_thread.Test07_InterruptPark - park...
22:16:59.181 [t1] INFO juc.t2_thread.Test07_InterruptPark - unPark...
22:16:59.181 [t1] INFO juc.t2_thread.Test07_InterruptPark - 打断状态:true
22:16:59.188 [t1] INFO juc.t2_thread.Test07_InterruptPark - unPark...
```
parkTest2()结果
```
22:12:36.878 [t2] INFO juc.t2_thread.Test07_InterruptPark - park...
22:12:37.881 [t2] INFO juc.t2_thread.Test07_InterruptPark - unPark...
22:12:37.881 [t2] INFO juc.t2_thread.Test07_InterruptPark - 打断状态:true
```
Thread.interrupted()静态方法，在执行过程中会将打断状态标记为false，所以LockSupport.park()会被重新打断，test2中就没有最终打印 "unPark..." 的日志。  
这是interrupted()和isInterrupted()区别  

park方法 在使用interrupt时会时效  

## 6.主线程与守护线程  
默认情况下，Java 进程需要等待所有线程都运行结束，才会结束。有一种特殊的线程叫做守护线程，只要其它非守
护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束。  

在start()之前 currentThread.setDaemon(true)则将线程设置为守护线程  
JVM GC线程则为守护线程  

## 7.线程状态  
### 7.1 从操作系统层面  
5种线程状态  
![image.png](https://note.youdao.com/yws/res/f/WEBRESOURCE4c549fc64d25b38a23ab25a3239dea1f)  
- 【初始状态】仅是在语言层面创建了线程对象，还未与操作系统线程关联  
- 【可运行状态】(就绪状态)指该线程已经被创建(与操作系统线程关联)，可以由 CPU 调度执行   
- 【运行状态】指获取了 CPU 时间片运行中的状态
当 CPU 时间片用完，会从【运行状态】转换至【可运行状态】，会导致线程的上下文切换   
- 【阻塞状态】
如果调用了阻塞 API，如 BIO 读写文件，这时该线程实际不会用到 CPU，会导致线程上下文切换，进入 【阻塞状态】
等 BIO 操作完毕，会由操作系统唤醒阻塞的线程，转换至【可运行状态】 与【可运行状态】的区别是，对【阻塞状态】的线程来说只要它们一直不唤醒，调度器就一直不会考虑 调度它们  
- 【终止状态】表示线程已经执行完毕，生命周期已经结束，不会再转换为其它状态


### 7.2 Java API层面  
根据Thread.State枚举  
```java
public enum State {
        
        NEW,

        RUNNABLE,

        BLOCKED,

        WAITING,

        TIMED_WAITING,

        TERMINATED;
    }
```
![image.png](https://note.youdao.com/yws/res/3/WEBRESOURCE6e39bf3b318e61c06836c90debe4cc23)  
Jvm管理这些状态
1. new(初始状态)  
    新创建的线程的初始状态  
2. Runnable(就绪状态)  
    Ready：等待cpu执行  
    Running：已经在执行
3. Teminated(终止状态)  
    结束状态
4. TimedWaiting(有时效性等待)  
5. Waiting(等待状态)  
6. Blocked(阻塞状态)  
    synchronized等被阻塞

- NEW 线程刚被创建，但是还没有调用 start() 方法  
- RUNNABLE 当调用了 start() 方法之后，注意，Java API 层面的 RUNNABLE 状态涵盖了 操作系统 层面的
【可运行状态】、【运行状态】和【阻塞状态】(由于 BIO 导致的线程阻塞，在 Java 里无法区分，仍然认为 是可运行)  
- BLOCKED ， WAITING ， TIMED_WAITING 都是 Java API 层面对【阻塞状态】的细分  
- TERMINATED 当线程代码运行结束  

代码测试  
```java
/**
 * @author fanglingxiao
 * @version 1.0
 * @description 线程6种状态
 * @date 2021/10/19 10:49 下午
 **/
@Slf4j
public class Test08_ThreadState {
    public static void main(String[] args) {
        // running
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
//                log.info("Running...");
            }
        };
//        t1.start();
        // runnable
        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while (true) {
                    // runnable
                }
            }
        };
        t2.start();

        // Teminated(因为执行完后主线程sleep后打印state则已执行完)
        Thread t3 = new Thread("t3") {
            @Override
            public void run() {
//                log.info("Running...");
            }
        };
        t3.start();

        // timed_waiting
        Thread t4 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (Test08_ThreadState.class) {
                    try {
//                        log.info("timed_waiting");
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t4.start();

        Thread t5 = new Thread("t5") {
            @Override
            public void run() {
                try {
//                    log.info("Waiting...");
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t5.start();

        Thread t6 = new Thread("t6") {
            @Override
            public void run() {
//                log.info("blocked...");
                synchronized (Test08_ThreadState.class) {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t6.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("t1状态：{}", t1.getState());
        log.info("t2状态：{}", t2.getState());
        log.info("t3状态：{}", t3.getState());
        log.info("t4状态：{}", t4.getState());
        log.info("t5状态：{}", t5.getState());
        log.info("t6状态：{}", t6.getState());
    }
}
```
运行结果：
```
23:07:57.439 [main] INFO juc.t2_thread.Test08_ThreadState - t1状态：NEW
23:07:57.446 [main] INFO juc.t2_thread.Test08_ThreadState - t2状态：RUNNABLE
23:07:57.447 [main] INFO juc.t2_thread.Test08_ThreadState - t3状态：TERMINATED
23:07:57.447 [main] INFO juc.t2_thread.Test08_ThreadState - t4状态：TIMED_WAITING
23:07:57.447 [main] INFO juc.t2_thread.Test08_ThreadState - t5状态：WAITING
23:07:57.447 [main] INFO juc.t2_thread.Test08_ThreadState - t6状态：BLOCKED
```
## 8.统筹应用  
### 8.1 烧开水  
阅读华罗庚《统筹方法》，给出烧水泡茶的多线程解决方案  
1. 洗水壶、洗茶壶、洗茶杯、拿茶叶各1分钟  
2. 烧开水15分钟  

提示：
- 参考图二，用两个线程(两个人协作)模拟烧水泡茶过程
文中办法乙、丙都相当于任务串行 而图一相当于启动了 4 个线程，有点浪费  
- 用 sleep(n) 模拟洗茶壶、洗水壶等耗费的时间  

方案：  
![image.png](https://note.youdao.com/yws/res/9/WEBRESOURCE809f1ec320d47d7f5d2c6478487d7729)  
```java
/**
 * @author fanglingxiao
 * @version 1.0
 * @description 统筹烧开水案例
 * @date 2021/10/19 11:22 下午
 **/
@Slf4j
public class Test09_BoilWaterTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = washKettle(1);
        Thread t2 = boilWater(15, t1);
        Thread t3 = operateOthers(3, t1);
        t1.start();
        t2.start();
        t3.start();
        t2.join();
        log.info("泡茶");
    }

    private static Thread operateOthers(int min, Thread t) {
        return new Thread(() -> {
            try {
                t.join();
                log.info("洗茶壶、洗茶杯、拿茶叶耗时：" + min + "分钟");
                Thread.sleep(min * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "operateOthers");
    }

    private static Thread boilWater(int min, Thread t) {
        return new Thread(() -> {
            try {
                t.join();
                log.info("烧开水耗时：" + min + "分钟");
                Thread.sleep(min * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "boilWater");
    }

    private static Thread washKettle(int min) {
        return new Thread(() -> {
            log.info("洗茶壶耗时：" + min + "分钟");
            try {
                Thread.sleep(min * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "washKettle");
    }
}
```
运行结果：
```
23:38:12.109 [washKettle] INFO juc.t2_thread.Test09_BoilWaterTest - 洗茶壶耗时：1分钟
23:38:13.113 [boilWater] INFO juc.t2_thread.Test09_BoilWaterTest - 烧开水耗时：15分钟
23:38:13.113 [operateOthers] INFO juc.t2_thread.Test09_BoilWaterTest - 洗茶壶、洗茶杯、拿茶叶耗时：3分钟
23:38:28.117 [main] INFO juc.t2_thread.Test09_BoilWaterTest - 泡茶
```


