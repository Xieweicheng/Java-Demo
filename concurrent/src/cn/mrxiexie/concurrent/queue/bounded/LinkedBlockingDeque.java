package cn.mrxiexie.concurrent.queue.bounded;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

/**
 * LinkedBlockingDeque是一个由链表结构组成的双向阻塞队列。
 * 所谓双向队列指的是可以从队列的两端插入和移出元素。
 * 双向队列因为多了一个操作队列的入口，在多线程同时入队时，也就减少了一半的竞争。
 * 相比其他的阻塞队列，LinkedBlockingDeque多了addFirst、addLast、offerFirst、offerLast、peekFirst和peekLast等方法，
 * 以First单词结尾的方法，表示插入、获取（peek）或移除双端队列的第一个元素。
 * 以Last单词结尾的方法，表示插入、获取或移除双端队列的最后一个元素。
 * 另外，插入方法add等同于addLast，移除方法remove等效于removeFirst。
 * 但是take方法却等同于takeFirst，不知道是不是JDK的bug，使用时还是用带有First和Last后缀的方法更清楚。
 * <p>
 * 双向阻塞队列可以运用在“工作窃取”模式中。
 *
 * @author mrxiexie
 */
public class LinkedBlockingDeque {

    private static java.util.concurrent.LinkedBlockingDeque<Integer> linkedBlockingDeque
            = new java.util.concurrent.LinkedBlockingDeque<>(10);

    public static void main(String[] args) {

        linkedBlockingDeque.addFirst(1);
        linkedBlockingDeque.addLast(9);

        ThreadUtils.log(String.valueOf(linkedBlockingDeque.removeFirst()));
        ThreadUtils.log(String.valueOf(linkedBlockingDeque.removeFirst()));
    }
}
