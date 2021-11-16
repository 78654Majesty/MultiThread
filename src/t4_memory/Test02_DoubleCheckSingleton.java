package t4_memory;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 单例设计模式-双检索
 * @date 2021/11/16 9:17 下午
 **/
public class Test02_DoubleCheckSingleton {
    private static volatile Test02_DoubleCheckSingleton instance = null;
    public static Test02_DoubleCheckSingleton getInstance(){
        if (instance == null){
            synchronized (Test02_DoubleCheckSingleton.class){
                if (instance == null){
                    instance = new Test02_DoubleCheckSingleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        getInstance();
    }
}
