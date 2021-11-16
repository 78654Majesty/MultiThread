package t3_monitor;

import common.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description 设计模式-保护性暂停
 * @date 2021/11/8 10:09 下午
 **/
@Slf4j
public class Test11_ProtectPause {
    public static void main(String[] args) {
        GuardedObject go = new GuardedObject();
        new Thread(() -> {
            try {
                log.info("等待结果。。。");
                List<String> list = (List<String>) go.get();
                log.info("结果大小size={}", list.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1").start();
        new Thread(() -> {
            try {
                log.info("执行下载。。。");
                List<String> list = Downloader.download();
                go.complete(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t2").start();
    }
}

class GuardedObject {
    private Object response;

    public Object get() throws InterruptedException {
        synchronized (this) {
            while (response == null) {
                this.wait();
            }
        }
        return response;
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}
