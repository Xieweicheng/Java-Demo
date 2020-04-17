package cn.mrxiexie.concurrent.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mrxiexie
 */
public class Exchanger {

    private static java.util.concurrent.Exchanger<String> exchanger = new java.util.concurrent.Exchanger<>();


    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        threadPool.execute(() -> {
            try {
                String A = "银行流水A";// A录入银行流水数据
                exchanger.exchange(A);
            } catch (InterruptedException ignored) {
            }
        });
        threadPool.execute(() -> {
            try {
                String B = "银行流水B";// B录入银行流水数据
                String A = exchanger.exchange("B");
                System.out.println("A和B数据是否一致：" + A.equals(B) + "，A录入的是："
                        + A + "，B录入是：" + B);
            } catch (InterruptedException ignored) {
            }
        });
        threadPool.shutdown();
    }
}
