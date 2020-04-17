package cn.mrxiexie.concurrent.lock;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁不公平锁
 *
 * @author mrxiexie
 * @date 19-12-16 上午10:34
 */
public class FairLock {

    private static int count = 10;
    /**
     * true  为公平锁：按获取锁顺序执行
     * false 为不公平锁：已在队列中的按顺序执行，不在队列中的线程会与在队列中的竞争锁
     * <p>
     * 可以通过观察日志输出，若 1~10 线程之间有穿插 10 以上的线程，说明为不公平锁
     */
    private static ReentrantLock lock = new ReentrantLock(false);
    private static CountDownLatch latch = new CountDownLatch(count * 2);
    private static int index;

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            lock.lock();
            try {
                ThreadUtils.log("wait 5 second");
                ThreadUtils.second(5);
            } finally {
                ThreadUtils.log("end");
                lock.unlock();
                for (int i = 0; i < count; i++) {
                    index++;
                    new FairThread(index).start();
                }
            }
        };

        new Thread(runnable).start();

        ThreadUtils.second(1);
        for (int i = 0; i < count; i++) {
            index++;
            new FairThread(index).start();
            // 确保 1~10 线程顺序添加到同步队列
            ThreadUtils.millisecond(50);
        }
        latch.await();
    }

    public static class FairThread extends Thread {

        private int index;

        public FairThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            super.run();
            String tag = "Thread" + index;
//            ThreadUtils.log(tag, "run");
            lock.lock();
            try {
                ThreadUtils.log(tag, "get lock");
            } finally {
                lock.unlock();
            }
            latch.countDown();
        }
    }
}
