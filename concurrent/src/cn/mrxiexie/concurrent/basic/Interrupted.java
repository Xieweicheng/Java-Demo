package cn.mrxiexie.concurrent.basic;

import java.util.concurrent.TimeUnit;

/**
 * 中断
 *
 * @author mrxiexie
 * @date 19-12-12 下午3:56
 */
public class Interrupted {

    public static void main(String[] args) throws Exception {

        ThreadUtils.log("begin");
        // sleepThread不停的尝试睡眠
        Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
        sleepThread.setDaemon(true);
        // busyThread不停的运行
        Thread busyThread = new Thread(new BusyRunner(), "BusyThread");
        busyThread.setDaemon(true);

        sleepThread.start();
        busyThread.start();
        // 休眠5秒，让sleepThread和busyThread充分运行
        TimeUnit.SECONDS.sleep(5);

        sleepThread.interrupt();
        busyThread.interrupt();

        // 返回 false，抛出 InterruptedException 异常之前，Java虚拟机会先将该线程的中断标识位清除
        ThreadUtils.log("SleepThread interrupted is " + sleepThread.isInterrupted());
        ThreadUtils.log("BusyThread interrupted is " + busyThread.isInterrupted());

        // 防止sleepThread和busyThread立刻退出
        ThreadUtils.second(2);
    }

    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                ThreadUtils.second(10);
            }
        }
    }

    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
            }
        }
    }
}
