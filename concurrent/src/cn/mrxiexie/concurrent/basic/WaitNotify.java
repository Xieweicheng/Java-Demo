package cn.mrxiexie.concurrent.basic;

/**
 * 1）使用wait()、notify()和notifyAll()时需要先对调用对象加锁。
 * 2）调用wait()方法后，线程状态由RUNNING变为WAITING，并将当前线程放置到对象的等待队列。
 * 3）notify()或notifyAll()方法调用后，等待线程依旧不会从wait()返回，需要调用notify()或notifyAll()的线程释放锁之后，等待线程才有机会从wait()返回。
 * 4）notify()方法将等待队列中的一个等待线程从等待队列中移到同步队列中，而notifyAll()方法则是将等待队列中所有的线程全部移到同步队列，被移动的线程状态由WAITING变为BLOCKED。
 * 5）从wait()方法返回的前提是获得了调用对象的锁。
 *
 * @author mrxiexie
 * @date 19-10-18 上午9:50
 */
public class WaitNotify {

    private static boolean flag = true;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        WaitThread waitThread = new WaitThread();
        waitThread.setName("WaitThread");
        waitThread.start();
        ThreadUtils.second(1);
        NotifyThread notifyThread = new NotifyThread();
        notifyThread.setName("NotifyThread");
        notifyThread.start();
    }

    public static class WaitThread extends Thread {
        @Override
        public void run() {
            super.run();
            // 加锁，拥有了 lock 的 monitor
            synchronized (lock) {
                while (flag) {
                    try {
                        ThreadUtils.log("flag is true. wait");
                        // 释放锁，线程从 RUNNING 转为 WAITING，线程放入等待队列（WaitQueue）
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            ThreadUtils.log("flag is false. running");
        }
    }

    public static class NotifyThread extends Thread {
        @Override
        public void run() {
            super.run();
            // 加锁，拥有了 lock 的 monitor
            synchronized (lock) {
                ThreadUtils.log("hold lock. notify");
                // 获取lock的锁，然后进行通知，通知时不会释放lock的锁，
                // 直到当前线程释放了lock后，WaitThread才能从wait方法中返回
                lock.notifyAll();
                flag = false;
                ThreadUtils.second(5);
            }

            synchronized (lock) {
                ThreadUtils.log("hold lock again. sleep");
                ThreadUtils.second(5);
            }
        }
    }
}
