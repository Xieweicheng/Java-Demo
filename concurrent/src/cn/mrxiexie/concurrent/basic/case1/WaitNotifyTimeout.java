package cn.mrxiexie.concurrent.basic.case1;

import java.util.LinkedList;

/**
 * 等待超时模式
 *
 * @author mrxiexie
 * @date 19-10-18 下午4:22
 */
public class WaitNotifyTimeout {

    private static LinkedList<Object> list = new LinkedList<>();

    public static void main(String[] args) {
    }

    public WaitNotifyTimeout(int count) {
        for (int i = 0; i < count; i++) {
            list.addLast(new Object());
        }
    }

    public synchronized Object fetch(long mills) throws InterruptedException {
        if (mills <= 0) {
            // 不设置超时
            while (list.isEmpty()) {
                wait();
            }
            return list.removeFirst();
        } else {
            // 设置超时
            long future = System.currentTimeMillis() + mills;
            // 剩余时间
            long remaining = mills;
            // 当超时大于0并且result返回值不满足要求
            while (list.isEmpty() && remaining > 0) {
                wait(remaining);
                remaining = future - System.currentTimeMillis();
            }
            if (!list.isEmpty()) {
                return list.removeFirst();
            }
            return null;
        }
    }

    public synchronized void release(Object obj) {
        list.add(obj);
        list.notifyAll();
    }
}
