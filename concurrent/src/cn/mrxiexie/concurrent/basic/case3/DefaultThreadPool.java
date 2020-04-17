package cn.mrxiexie.concurrent.basic.case3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认线程池
 *
 * @author mrxiexie
 * @date 19-10-19 上午9:41
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    /**
     * 最大工作者线程数量
     */
    private static final int MAX_WORKER_NUMBERS = 100;

    /**
     * 默认工作者线程数量
     */
    private static final int DEFAULT_WORKER_NUMBERS = 10;

    /**
     * 最小工作者线程数量
     */
    private static final int MIN_WORKER_NUMBERS = 1;

    /**
     * 工作者线程数量
     */
    private int workerNum;

    /**
     * 线程编号
     */
    private AtomicInteger threadNo = new AtomicInteger();

    /**
     * 这是一个工作列表，将会向里面插入工作
     */
    private final LinkedList<Job> jobs = new LinkedList<>();

    /**
     * 工作者列表
     */
    private List<Worker> workers = Collections.synchronizedList(new ArrayList<>());

    public DefaultThreadPool() {
        this(DEFAULT_WORKER_NUMBERS);
    }

    public DefaultThreadPool(int workerNum) {
        this.workerNum = workerNum > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS
                : workerNum < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : DEFAULT_WORKER_NUMBERS;
        initializeWorkers(workerNum);
    }

    @Override
    public void execute(Job job) {
        if (job != null) {
            synchronized (jobs) {
                jobs.add(job);
                // 添加一个Job后，对工作队列jobs调用了其notify()方法，而不是notifyAll()方法，因为能够确定有工作者线程被唤醒，
                // 这时使用notify()方法将会比notifyAll()方法获得更小的开销（避免将等待队列中的线程全部移动到阻塞队列中）。
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }

    @Override
    public void addWorkers(int num) {
        if (num < 0) {
            throw new UnsupportedOperationException();
        } else {
            synchronized (jobs) {
                if (workerNum + num > MAX_WORKER_NUMBERS) {
                    num = MAX_WORKER_NUMBERS - workerNum;
                }
                initializeWorkers(num);
                workerNum += num;
            }
        }
    }

    @Override
    public void removeWorkers(int num) {
        if (num < 0 || num >= workerNum) {
            throw new UnsupportedOperationException();
        } else {
            synchronized (jobs) {
                int count = 0;
                while (num > count) {
                    Worker worker = workers.get(count);
                    if (workers.remove(worker)) {
                        worker.shutdown();
                        count++;
                    }
                }
                workerNum -= num;
            }
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    /**
     * 初始化工作现场
     *
     * @param workerNum 工作线程数量
     */
    private void initializeWorkers(int workerNum) {
        for (int i = 0; i < workerNum; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread workerThread = new Thread(worker, String.format("ThreadPool-Worker-%s", threadNo.incrementAndGet()));
            workerThread.start();
        }
    }

    private class Worker implements Runnable {

        private boolean running = true;

        @Override
        public void run() {
            while (running) {
                Job job;
                synchronized (jobs) {
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            // 感知到外部对 WorkerThread 的中断操作，返回
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    job = jobs.removeFirst();
                }

                if (job != null) {
                    try {
                        job.run();
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        private void shutdown() {
            running = false;
        }
    }
}
