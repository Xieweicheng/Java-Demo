package cn.mrxiexie.concurrent.queue.unbounded;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

/**
 * LinkedTransferQueue是一个由链表结构组成的无界阻塞TransferQueue队列。相对于其他阻塞队列，
 * LinkedTransferQueue多了tryTransfer和transfer方法。
 *
 * @author mrxiexie
 */
public class LinkedTransferQueue {

    private static java.util.concurrent.LinkedTransferQueue<Integer> linkedTransferQueue
            = new java.util.concurrent.LinkedTransferQueue<>();

    public static void main(String[] args) {

        linkedTransferQueue.put(1);
        linkedTransferQueue.put(2);
        linkedTransferQueue.put(3);
        linkedTransferQueue.put(4);
        linkedTransferQueue.put(5);

        new TransferThread().start();
        new RemoveThread().start();

    }

    private static class TransferThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                ThreadUtils.log("transfer");
                /**
                 * tryTransfer方法是用来试探生产者传入的元素是否能直接传给消费者。如果没有消费者等待接收元素，则返回false。
                 * 和transfer方法的区别是tryTransfer方法无论消费者是否接收，方法立即返回，而transfer方法是必须等到消费者消费了才返回。
                 *
                 * 对于带有时间限制的tryTransfer（E e，long timeout，TimeUnit unit）方法，试图把生产者传入的元素直接传给消费者，
                 * 但是如果没有消费者消费该元素则等待指定的时间再返回，如果超时还没消费元素，则返回false，如果在超时时间内消费了元素，则返回true。
                 */
//                boolean b = linkedTransferQueue.tryTransfer(100, 20, TimeUnit.SECONDS);
//                ThreadUtils.log("transfer end " + b);

                /**
                 * 如果当前有消费者正在等待接收元素（消费者使用take()方法或带时间限制的poll()方法时），
                 * transfer方法可以把生产者传入的元素立刻transfer（传输）给消费者。如果没有消费者在等待接收元素，
                 * transfer方法会将元素存放在队列的tail节点，并等到该元素被消费者消费了才返回
                 */
                linkedTransferQueue.transfer(101);
                ThreadUtils.log("transfer end");
            } catch (InterruptedException e) {
                ThreadUtils.logErr(e);
            }
        }
    }

    private static class RemoveThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (true) {
                ThreadUtils.second(2);
                Integer poll = linkedTransferQueue.poll();
                if (poll == null) {
                    return;
                }
                ThreadUtils.log(String.valueOf(poll));
            }
        }
    }
}
