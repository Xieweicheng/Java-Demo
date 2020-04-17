package cn.mrxiexie.concurrent.queue;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * <table>
 *   <tr>
 *     <th>{@link java.util.concurrent.BlockingQueue}</th>
 *     <th>Desc</th>
 *   </tr>
 *   <tr>
 *     <td>{@link ArrayBlockingQueue}</td>
 *     <td>一个由数组结构组成的有界阻塞队列</td>
 *   </tr>
 *   <tr>
 *     <td>{@link LinkedBlockingQueue}</td>
 *     <td>一个由链表结构组成的有界阻塞队列</td>
 *   </tr>
 *   <tr>
 *     <td>{@link PriorityBlockingQueue}</td>
 *     <td>一个支持优先级排序的无界阻塞队列</td>
 *   </tr>
 *   <tr>
 *     <td>{@link cn.mrxiexie.concurrent.queue.unbounded.DelayQueue}</td>
 *     <td>一个使用优先级队列实现的无界阻塞队列</td>
 *   </tr>
 *   <tr>
 *     <td>{@link cn.mrxiexie.concurrent.queue.bounded.SynchronousQueue}</td>
 *     <td>一个不存储元素的阻塞队列</td>
 *   </tr>
 *   <tr>
 *     <td>{@link LinkedTransferQueue}</td>
 *     <td>一个由链表结构组成的无界阻塞队列</td>
 *   </tr>
 *   <tr>
 *     <td>{@link cn.mrxiexie.concurrent.queue.bounded.LinkedBlockingDeque}</td>
 *     <td>一个由链表结构组成的双向阻塞队列</td>
 *   </tr>
 * </table>
 *
 * @author mrxiexie
 */
public class BlockingQueue {

    public static void main(String[] args) {

        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(5);
        arrayBlockingQueue.add(1);
        arrayBlockingQueue.add(2);
        arrayBlockingQueue.add(3);
        arrayBlockingQueue.add(4);
        arrayBlockingQueue.add(5);
        ThreadUtils.log("peek -> " + arrayBlockingQueue.peek());
        ThreadUtils.log("peek -> " + arrayBlockingQueue.peek());
        ThreadUtils.log("peek -> " + arrayBlockingQueue.peek());
        ThreadUtils.log("element -> " + arrayBlockingQueue.element());
        ThreadUtils.log("element -> " + arrayBlockingQueue.element());
        ThreadUtils.log("element -> " + arrayBlockingQueue.element());
        ThreadUtils.log("poll -> " + arrayBlockingQueue.poll());
        ThreadUtils.log("poll -> " + arrayBlockingQueue.poll());
        ThreadUtils.log("poll -> " + arrayBlockingQueue.poll());
        ThreadUtils.log("poll -> " + arrayBlockingQueue.poll());
        ThreadUtils.log("poll -> " + arrayBlockingQueue.poll());
        ThreadUtils.log("peek -> " + arrayBlockingQueue.peek());
        ThreadUtils.log("peek -> " + arrayBlockingQueue.peek());
        ThreadUtils.log("peek -> " + arrayBlockingQueue.peek());
        ThreadUtils.log("element -> " + arrayBlockingQueue.element());
        ThreadUtils.log("element -> " + arrayBlockingQueue.element());
        ThreadUtils.log("element -> " + arrayBlockingQueue.element());
    }
}
