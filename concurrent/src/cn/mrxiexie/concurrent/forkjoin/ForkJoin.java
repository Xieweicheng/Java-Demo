package cn.mrxiexie.concurrent.forkjoin;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mrxiexie
 */
public class ForkJoin extends RecursiveTask<Integer> {

    private static ThreadLocal<String> names = new ThreadLocal<>();
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 阈值
     */
    private static final int THRESHOLD = 100;
    private int start;
    private int end;

    public ForkJoin(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        synchronized (ForkJoin.class) {
            String s = names.get();
            if (s == null) {
                s = "线程 - " + atomicInteger.incrementAndGet();
                ThreadUtils.log("compute : " + s);
                Thread.currentThread().setName(s);
                names.set(s);
            }
        }

        int sum = 0;
        // 如果任务足够小就计算任务
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            ThreadUtils.log("Run start = " + start + " end = " + end);
            for (int i = start; i <= end; i++) {
                sum += i;
            }
            ThreadUtils.log("Run end");
        } else {
            // 如果任务大于阈值，就分裂成两个子任务计算
            int middle = (start + end) / 2;
//            sum = invoke(start, middle, end);
            sum = join(start, middle, end);
        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        // 生成一个计算任务，负责计算1+2+3+4
        ForkJoin task = new ForkJoin(1, 400);
        // 执行一个任务
        Future<Integer> result = forkJoinPool.submit(task);
        try {
            ThreadUtils.log(String.valueOf(result.get()));
        } catch (Exception e) {
            ThreadUtils.logErr(e);
        }
    }

    private static int invoke(int start, int middle, int end) {
        ForkJoin leftTask = new ForkJoin(start, middle);
        ForkJoin rightTask = new ForkJoin(middle + 1, end);
        ThreadUtils.log("invoke start = " + start + " middle = " + middle + " end = " + end);
        invokeAll(leftTask, rightTask);
        int leftResult = 0;
        int rightResult = 0;
        try {
            leftResult = leftTask.get();
            rightResult = rightTask.get();
        } catch (InterruptedException | ExecutionException e) {
            ThreadUtils.logErr(e);
        } finally {
            ThreadUtils.log("invoke end");
        }
        return leftResult + rightResult;
    }

    private static int join(int start, int middle, int end) {
        ForkJoin leftTask = new ForkJoin(start, middle);
        ForkJoin rightTask = new ForkJoin(middle + 1, end);
        ThreadUtils.log("join start = " + start + " middle = " + middle + " end = " + end);

        // 执行子任务
        leftTask.fork();
        rightTask.fork();
        // 等待子任务执行完，并得到其结果
        int leftResult = leftTask.join();
        int rightResult = rightTask.join();
        ThreadUtils.log("join end");
        // 合并子任务
        return leftResult + rightResult;
    }
}
