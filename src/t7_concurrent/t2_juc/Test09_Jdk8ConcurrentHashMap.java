package t7_concurrent.t2_juc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/12/14 10:47 下午
 **/
public class Test09_Jdk8ConcurrentHashMap {

    private static final Map<String, String> map = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        map.put("first","firstValue");
        map.get("first");
    }
}
