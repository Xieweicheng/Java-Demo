package cn.mrxiexie.concurrent.basic;

/**
 * 死锁
 *
 * @author mrxiexie
 */
public class Deadlock {

    private static final String A = "A";
    private static final String B = "B";

    public static void main(String[] args) {
        new Deadlock().deadLock();
    }

    private void deadLock() {
        Thread t1 = new Thread(() -> {
            synchronized (A) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {
                    System.out.println("1");
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (B) {
                synchronized (A) {
                    System.out.println("2");
                }
            }
        });
        t1.start();
        t2.start();
    }
}
