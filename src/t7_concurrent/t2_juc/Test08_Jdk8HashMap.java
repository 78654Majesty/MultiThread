package t7_concurrent.t2_juc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/12/14 10:11 下午
 **/
public class Test08_Jdk8HashMap {
    private static final Map<String, String> map = new HashMap<>();

    public static void main(String[] args) {
        map.put("1", "1");
    }
}
