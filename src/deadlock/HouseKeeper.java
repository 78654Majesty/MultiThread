package deadlock;

import java.util.ArrayList;
import java.util.List;

class HouseKeeper {
    private List<Object> keeper = new ArrayList<>();

    //一次性申请所有得资源
    synchronized boolean apply(Object from, Object to) {
        if (keeper.contains(from) || keeper.contains(to)) {
            return false;
        } else {
            keeper.add(from);
            keeper.add(to);
        }
        return true;
    }

    //释放资源
    synchronized void free(Object from, Object to) {
        keeper.remove(from);
        keeper.remove(to);
    }
}