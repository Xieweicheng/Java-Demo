package cn.mrxiexie.concurrent.lock;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mrxiexie
 */
public class Condition {

    private static Lock lock = new ReentrantLock();

    private static java.util.concurrent.locks.Condition condition = lock.newCondition();

    public static void main(String[] args) {
        new WaitThread().start();
        ThreadUtils.millisecond(50);
        new SignalThread().start();
    }

    private static class WaitThread extends Thread {
        @Override
        public void run() {
            super.run();
            lock.lock();
            try {
                ThreadUtils.log("wait");
                condition.await();
                ThreadUtils.log("wait end");
            } catch (InterruptedException e) {
                ThreadUtils.logErr(e);
            } finally {
                lock.unlock();
            }
        }
    }

    private static class SignalThread extends Thread {
        @Override
        public void run() {
            super.run();
            lock.lock();
            try {
                ThreadUtils.second(5);
                ThreadUtils.log("signal");
                condition.signal();
                ThreadUtils.log("signal end");
            } finally {
                lock.unlock();
                ThreadUtils.log("signal unlock");
            }
        }
    }

    /**
     * 有界队列是一种特殊的队列，当队列为空时，队列的获取操作将会阻塞获取线程，直到队列中有新增元素，当队列已满时，
     * 队列的插入操作将会阻塞插入线程，直到队列出现“空位”
     */
    private static class BoundedQueue<T> {
        private Object[] items;
        // 添加的下标，删除的下标和数组当前数量
        private int addIndex, removeIndex, count;
        private Lock lock = new ReentrantLock();
        private java.util.concurrent.locks.Condition notEmpty = lock.newCondition();
        private java.util.concurrent.locks.Condition notFull = lock.newCondition();

        public BoundedQueue(int size) {
            items = new Object[size];
        }

        public static void main(String[] args) {
            BoundedQueue<Integer> boundedQueue = new BoundedQueue<>(2);

            Runnable addRunnable = () -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        ThreadUtils.log("add " + i + " begin");
                        boundedQueue.add(i);
                        ThreadUtils.log("add " + i + " end");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            Runnable removeRunnable = () -> {
                try {
                    while (boundedQueue.count != 0) {
                        ThreadUtils.second(2);
                        ThreadUtils.log("remove " + boundedQueue.remove());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };

            new Thread(addRunnable).start();
            ThreadUtils.second(1);
            new Thread(removeRunnable).start();
        }

        /**
         * 添加一个元素，如果数组满，则添加线程进入等待状态，直到有"空位"
         */
        public void add(T t) throws InterruptedException {
            lock.lock();
            try {
                while (count == items.length) {
                    notFull.await();
                }
                items[addIndex] = t;
                if (++addIndex == items.length) {
                    addIndex = 0;
                }
                ++count;
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        /**
         * 由头部删除一个元素，如果数组空，则删除线程进入等待状态，直到有新添加元素
         */
        @SuppressWarnings("unchecked")
        public T remove() throws InterruptedException {
            lock.lock();
            try {
                while (count == 0) {
                    notEmpty.await();
                }
                Object x = items[removeIndex];
                if (++removeIndex == items.length) {
                    removeIndex = 0;
                }
                --count;
                notFull.signal();
                return (T) x;
            } finally {
                lock.unlock();
            }
        }
    }
}
