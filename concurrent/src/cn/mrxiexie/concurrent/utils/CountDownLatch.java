package cn.mrxiexie.concurrent.utils;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

/**
 * 等待多线程完成的CountDownLatch
 *
 * @author mrxiexie
 */
public class CountDownLatch {

    private static java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(5);


    public static void main(String[] args) {
        Runnable runnable = () -> {
            ThreadUtils.log("CountDown");
            countDownLatch.countDown();
        };

        for (int i = 0; i < 5; i++) {
            new Thread(runnable).start();
        }

        try {
            ThreadUtils.log("await");
            countDownLatch.await();
            ThreadUtils.log("await end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
