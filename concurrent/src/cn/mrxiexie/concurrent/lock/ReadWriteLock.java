package cn.mrxiexie.concurrent.lock;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 *
 * @author mrxiexie
 * @date 19-12-17 上午9:23
 */
public class ReadWriteLock {

    private static Map<String, Object> map = new HashMap<>();
    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(false);
    private static Lock r = rwl.readLock();
    private static Lock w = rwl.writeLock();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new ReadThread().start();
        }
        for (int i = 0; i < 10; i++) {
            new WriteThread().start();
        }
    }

    public static class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            r.lock();
            try {
                ThreadUtils.log("ReadThread lock");
                ThreadUtils.second(5);
            } finally {
                r.unlock();
            }
        }
    }

    public static class WriteThread extends Thread {

        @Override
        public void run() {
            super.run();
            w.lock();
            try {
                ThreadUtils.log("WriteThread lock");
                ThreadUtils.second(5);
            } finally {
                w.unlock();
            }
        }
    }

    /**
     * 写锁降级
     * <p>
     * 锁降级中读锁的获取是否必要呢？答案是必要的。主要是为了保证数据的可见性，如果当前线程不获取读锁而是直接释放写锁，
     * 假设此刻另一个线程（记作线程T）获取了写锁并修改了数据，那么当前线程无法感知线程T的数据更新。如果当前线程获取读锁，
     * 即遵循锁降级的步骤，则线程T将会被阻塞，直到当前线程使用数据并释放读锁之后，线程T才能获取写锁进行数据更新。
     */
    public static class CachedData {

        private Object data;
        // 保证状态可见性
        private volatile boolean cacheValid;
        private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        private ReentrantReadWriteLock.ReadLock readLock = rwl.readLock();
        private ReentrantReadWriteLock.WriteLock writeLock = rwl.writeLock();

        private void processCachedData() {
            readLock.lock();
            if (!cacheValid) {
                // 必须先释放读锁
                readLock.unlock();
                // 锁降级从写锁获取到开始
                writeLock.lock();
                try {
                    if (!cacheValid) {
                        // 准备数据的流程（略）
                        // data = xxx
                        data = null;
                        cacheValid = true;
                    }
                    // 写锁降级为读锁
                    readLock.lock();
                } finally {
                    writeLock.unlock();
                }
                // 锁降级完成，写锁降为读锁
            }
            try {
                // 使用数据的流程（略）
                use(data);
            } finally {
                readLock.unlock();
            }
        }

        private void use(Object object) {

        }
    }

    /**
     * 获取一个key对应的value
     */
    public static Object get(String key) {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    /**
     * 设置key对应的value，并返回旧的value
     */
    public static Object put(String key, Object value) {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }

    /**
     * 清空所有的内容
     */
    public static void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }
}
