package cn.mrxiexie.concurrent.queue.bounded;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

/**
 * LinkedBlockingQueue是一个用链表实现的有界阻塞队列。此队列的默认和最大长度为Integer.MAX_VALUE。此队列按照先进先出的原则对元素进行排序。
 *
 * @author mrxiexie
 */
public class LinkedBlockingQueue {

    private static java.util.concurrent.LinkedBlockingQueue<Integer> linkedBlockingQueue =
            new java.util.concurrent.LinkedBlockingQueue<>();

    public static void main(String[] args) {
        linkedBlockingQueue.add(1);
        linkedBlockingQueue.add(2);
        linkedBlockingQueue.add(3);
        linkedBlockingQueue.add(4);
        linkedBlockingQueue.add(5);
        ThreadUtils.log(String.valueOf(linkedBlockingQueue.remove()));
        ThreadUtils.log(String.valueOf(linkedBlockingQueue.remove()));
        ThreadUtils.log(String.valueOf(linkedBlockingQueue.remove()));
        ThreadUtils.log(String.valueOf(linkedBlockingQueue.remove()));
        ThreadUtils.log(String.valueOf(linkedBlockingQueue.remove()));
    }
}
