package cn.mrxiexie.concurrent.utils;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mrxiexie
 */
public class Semaphore {

    private static java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(5);

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {

        Runnable runnable = () -> {
            try {
                ThreadUtils.log("acquire ");
                semaphore.acquire();
                ThreadUtils.log("acquire end " + atomicInteger.incrementAndGet());
                ThreadUtils.second(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
                ThreadUtils.log("release");
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }


    }
}
