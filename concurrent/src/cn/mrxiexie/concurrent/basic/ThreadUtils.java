package cn.mrxiexie.concurrent.basic;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * @author mrxiexie
 * @date 19-10-18 下午2:15
 */
public class ThreadUtils {

    public static void log(String tag, String text) {
        System.out.printf("[%-20s] %-12s %-20s - %s%n", tag, LocalTime.now(), Thread.currentThread().getName(), text);
    }

    public static void log(String text) {
        System.out.printf("%-12s %-20s - %s%n", LocalTime.now(), Thread.currentThread().getName(), text);
    }

    public static void logErr(Exception e) {
        e.printStackTrace();
        logErr(e.getMessage());
    }

    public static void logErr(String text) {
        System.err.println(String.format("%s %-20s - %s%n", LocalTime.now(), Thread.currentThread().getName(), text));
    }

    public static void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            logErr(e);
        }
    }

    public static void millisecond(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            logErr(e);
        }
    }
}
