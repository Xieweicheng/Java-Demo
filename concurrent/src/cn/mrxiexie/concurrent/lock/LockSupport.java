package cn.mrxiexie.concurrent.lock;

/**
 * @author mrxiexie
 */
public class LockSupport {

    private static Object lock = new Object();

    /**
     * 使用 `jps | grep LockSupport | awk '{print $1}' | xargs jstack` 查看堆栈信息
     * <p>
     * {@link java.util.concurrent.locks.LockSupport#park(Object)} 会在堆栈信息中显示 blocker 的信息
     */
    public static void main(String[] args) {
        new ParkThread("LockSupport Park").start();
        java.util.concurrent.locks.LockSupport.park();
    }

    private static class ParkThread extends Thread {

        public ParkThread(String name) {
            setName(name);
        }

        @Override
        public void run() {
            super.run();

            java.util.concurrent.locks.LockSupport.park(lock);
        }
    }
}
