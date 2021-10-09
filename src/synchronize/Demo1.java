package synchronize;

/**
 * @author james
 */
public class Demo1 {
    private static int count = 0;
    private static Object object = new Object();
    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();
        Thread t1 = new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        },"t1");
        Thread t2 = new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        },"t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }
}

class Room{
    private int count = 0;
    public void decrement(){
        synchronized (this){
            count--;
        }
    }
    public void increment(){
        synchronized (this){
            count++;
        }
    }
    public synchronized int getCount(){
        return count;
    }
}
