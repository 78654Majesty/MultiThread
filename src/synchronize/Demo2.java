package synchronize;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author james
 */
public class Demo2 {
    public static void main(String[] args) {
        Number number = new Number();
        new Thread(()->{
            try {
                number.a();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            number.b();
        }).start();
    }
}

class Number{
    public synchronized void a() throws InterruptedException {
        // sleep不会释放锁
        Thread.sleep(1);
        System.out.println("a");
    }

    public synchronized void b() {
        System.out.println("b");
    }
}
