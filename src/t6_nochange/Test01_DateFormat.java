package t6_nochange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/21 10:16 下午
 **/
public class Test01_DateFormat {

    public static void main(String[] args) {
//        simpleDateFormatTest();
        dateTimeFormatTest();
    }

    public static void dateTimeFormatTest() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(dtf.parse("2021-11-21"));
            }).start();
        }
    }

    /**
     * SimpleDateFormat线程不安全
     */
    public static void simpleDateFormatTest() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (sdf) {
                    try {
                        System.out.println(sdf.parse("2021-11-21"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
