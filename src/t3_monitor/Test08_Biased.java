package t3_monitor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 偏向锁
 * @date 2021/11/1 10:43 下午
 **/
@Slf4j
public class Test08_Biased {
    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
        log.info(JSONObject.toJSONString(ClassLayout.parseInstance(dog).toPrintable(dog)));
        Thread.sleep(4000);
        Dog dog1 = new Dog();
        log.info(JSONObject.toJSONString(ClassLayout.parseInstance(dog1).toPrintable(dog1)));
    }
}
class Dog{

}
