package cn.mrxiexie.concurrent.basic;

/**
 * 如果一个线程A执行了thread.join()语句，其含义是：当前线程A等待thread线程终止之后才从thread.join()返回。
 * 线程Thread除了提供join()方法之外，还提供了join(long millis)和join(long millis,int nanos)两个具备超时特性的方法。
 * 这两个超时方法表示，如果线程thread在给定的超时时间里没有终止，那么将会从该超时方法中返回。
 *
 * @author mrxiexie
 * @date 19-10-18 上午9:50
 */
public class Join {

    public static void main(String[] args) {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            // 每个线程拥有前一个线程的引用，需要等待前一个线程终止，才能从等待中返回
            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            thread.start();
            previous = thread;
        }
        ThreadUtils.second(5);
        ThreadUtils.log("terminate.");
    }

    static class Domino implements Runnable {
        private Thread thread;

        public Domino(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                ThreadUtils.log(thread.getName() + ".join() invoke");
                thread.join();
                ThreadUtils.second(2);
            } catch (InterruptedException ignored) {
            }
            ThreadUtils.log("terminate.");
        }
    }
}
