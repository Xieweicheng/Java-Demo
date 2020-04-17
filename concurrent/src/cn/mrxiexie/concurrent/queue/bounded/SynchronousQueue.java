package cn.mrxiexie.concurrent.queue.bounded;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

/**
 * SynchronousQueue是一个不存储元素的阻塞队列。每一个put操作必须等待一个take操作，否则不能继续添加元素。
 * <p>
 * 它支持公平访问队列。默认情况下线程采用非公平性策略访问队列。使用以下构造方法可以创建公平性访问的SynchronousQueue，
 * 如果设置为true，则等待的线程会采用先进先出的顺序访问队列。
 * <p>
 * SynchronousQueue可以看成是一个传球手，负责把生产者线程处理的数据直接传递给消费者线程。
 * 队列本身并不存储任何元素，非常适合传递性场景。SynchronousQueue的吞吐量高于LinkedBlockingQueue和ArrayBlockingQueue。
 *
 * @author mrxiexie
 */
public class SynchronousQueue {

    private static java.util.concurrent.SynchronousQueue<Integer> synchronousQueue = new java.util.concurrent.SynchronousQueue<>();

    /**
     * 使用 put 的线程会阻塞直到有 take 才会放行，反之亦然unbounded
     */
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                ThreadUtils.log("put");
                synchronousQueue.put(1);
                ThreadUtils.log("put end");
            } catch (InterruptedException e) {
                ThreadUtils.logErr(e);
            }
        }).start();

        new Thread(() -> {
            final Integer take;
            try {
                ThreadUtils.log("take");
                take = synchronousQueue.take();
                ThreadUtils.log("take end");
                ThreadUtils.log(String.valueOf(take));
            } catch (InterruptedException e) {
                ThreadUtils.logErr(e);
            }
        }).start();
    }
}
