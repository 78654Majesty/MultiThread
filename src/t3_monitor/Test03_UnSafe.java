package t3_monitor;

import java.util.Hashtable;
import java.util.Objects;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/10/27 5:13 下午
 **/
public class Test03_UnSafe {
    public static void main(String[] args) {
        Hashtable<String, String> map = new Hashtable<>();
        if (null == map.get("1")) {
            map.put("1", "1");
        }
        map.putIfAbsent("1", "1");
//        MapOpt opt = new MapOpt();
//        if (Objects.isNull(opt.get("1", map))) {
//            opt.put("1", "1", map);
//        }
    }
}

class MapOpt {
    public void put(String key, String value, Hashtable<String, String> map) {
        map.put(key, value);
    }

    public String get(String key, Hashtable<String, String> map) {
        return map.get(key);
    }
}
