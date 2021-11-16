package t4_memory;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 内存可见行问题
 * @date 2021/11/12 4:13 下午
 **/
public class Test01_VisibleProblem {
    private static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (true) {
                if (!flag){
                    break;
                }
            }
        }).start();
        Thread.sleep(1000);
        flag = true; // 不生效
    }
}
