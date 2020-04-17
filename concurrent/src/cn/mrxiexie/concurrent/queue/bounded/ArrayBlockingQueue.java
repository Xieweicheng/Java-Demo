package cn.mrxiexie.concurrent.queue.bounded;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

/**
 * ArrayBlockingQueue是一个用数组实现的有界阻塞队列。此队列按照先进先出（FIFO）的原则对元素进行排序。
 * <p>
 * 默认情况下不保证线程公平的访问队列，所谓公平访问队列是指阻塞的线程，可以按照阻塞的先后顺序访问队列，即先阻塞线程先访问队列。
 * 非公平性是对先等待的线程是非公平的，当队列可用时，阻塞的线程都可以争夺访问队列的资格，有可能先阻塞的线程最后才访问队列。
 * 为了保证公平性，通常会降低吞吐量。
 *
 * @author mrxiexie
 */
public class ArrayBlockingQueue {

    private static java.util.concurrent.ArrayBlockingQueue<Integer> arrayBlockingQueue =
            new java.util.concurrent.ArrayBlockingQueue<>(5, false);

    public static void main(String[] args) {
        arrayBlockingQueue.add(1);
        arrayBlockingQueue.add(2);
        arrayBlockingQueue.add(3);
        arrayBlockingQueue.add(4);
        arrayBlockingQueue.add(5);
        ThreadUtils.log(String.valueOf(arrayBlockingQueue.remove()));
        ThreadUtils.log(String.valueOf(arrayBlockingQueue.remove()));
        ThreadUtils.log(String.valueOf(arrayBlockingQueue.remove()));
        ThreadUtils.log(String.valueOf(arrayBlockingQueue.remove()));
        ThreadUtils.log(String.valueOf(arrayBlockingQueue.remove()));
    }
}
