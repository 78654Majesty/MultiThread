package semaphore;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * description
 * 利用Semaphore信号量模拟对象池
 * @author fanglingxiao
 * @date 2019/8/8
 */
public class SemaphoreTest<T,R> {
    private final List<T> pool;
    private final Semaphore semaphore;

    public SemaphoreTest(int size,T t){
        pool = new Vector<>();
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        semaphore = new Semaphore(size);
    }

    // 利用对象池的对象，调用func
    public R exec(Function<T,R> func){
        T t = null;
        try{
            semaphore.acquire();
            t = pool.remove(0);
            return func.apply(t);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            pool.add(t);
            semaphore.release();
        }
        return func.apply(t);
    }

    public static void main(String[] args) {
        SemaphoreTest semaphoreTest = new SemaphoreTest<Long,String>(10,2L);
        
    }
}
