package cn.mrxiexie.concurrent.utils;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

import java.util.concurrent.BrokenBarrierException;

/**
 * 同步屏障CyclicBarrier
 *
 * @author mrxiexie
 */
public class CyclicBarrier {

    private static java.util.concurrent.CyclicBarrier cyclicBarrier = new java.util.concurrent.CyclicBarrier(5, () -> {
        ThreadUtils.log("我自闭了");
    });

    public static void main(String[] args) {

        Runnable runnable = () -> {
            ThreadUtils.log("cyclicBarrier await");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            } finally {
                ThreadUtils.log("cyclicBarrier await end");
            }
        };

        for (int i = 0; i < 5; i++) {
            new Thread(runnable).start();
        }
    }


}
