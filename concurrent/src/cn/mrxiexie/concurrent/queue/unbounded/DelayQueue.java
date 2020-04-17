package cn.mrxiexie.concurrent.queue.unbounded;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.PriorityQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * {@link java.util.concurrent.DelayQueue} 是一个支持延时获取元素的 **无界阻塞队列**。队列使用 {@link PriorityQueue} 来实现。
 * 队列中的元素必须实现 {@link Delayed} 接口，在创建元素时可以指定多久才能从队列中获取当前元素。只有在延迟期满时才能从队列中提取元素。
 * {@link java.util.concurrent.DelayQueue} 非常有用，可以将 {@link java.util.concurrent.DelayQueue} 运用在以下应用场景。
 * <p>
 * <p>
 * 1、缓存系统的设计：可以用 {@link java.util.concurrent.DelayQueue} 保存缓存元素的有效期，使用一个线程循环查询 {@link java.util.concurrent.DelayQueue} ，
 * 一旦能从 {@link java.util.concurrent.DelayQueue}  中获取元素时，表示缓存有效期到了。
 * 2、定时任务调度：使用 {@link java.util.concurrent.DelayQueue} 保存当天将会执行的任务和执行时间，
 * 一旦从 {@link java.util.concurrent.DelayQueue} 中获取到任务就开始执行，
 * 比如 {@link javax.swing.TimerQueue} 就是使用 {@link java.util.concurrent.DelayQueue} 实现的。
 *
 * @author mrxiexie
 */
public class DelayQueue {

    private static java.util.concurrent.DelayQueue<Item> delayQueue = new java.util.concurrent.DelayQueue<>();

    public static void main(String[] args) {
        delayQueue.offer(new Item(LocalDateTime.now().plusSeconds(15).toInstant(ZoneOffset.of("+8")).toEpochMilli(), "3"));
        delayQueue.offer(new Item(LocalDateTime.now().plusSeconds(5).toInstant(ZoneOffset.of("+8")).toEpochMilli(), "1"));
        new Thread(() -> {
            ThreadUtils.second(5);
            delayQueue.offer(new Item(LocalDateTime.now().plusSeconds(10).toInstant(ZoneOffset.of("+8")).toEpochMilli(), "2"));
        }).start();

        Runnable runnable = () -> {
            while (true) {
                Item poll;
                try {
                    poll = delayQueue.poll(20, TimeUnit.SECONDS);
                    if (poll == null) {
                        return;
                    }
                    ThreadUtils.log(poll.getName());
                } catch (InterruptedException e) {
                    ThreadUtils.logErr(e);
                }
            }
        };
        new Thread(runnable).start();
    }

    private static class Item implements Delayed {

        private long time;

        private String name;

        private Item(long time, String name) {
            this.time = time;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - System.currentTimeMillis(), TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(getDelay(TimeUnit.NANOSECONDS), o.getDelay(TimeUnit.NANOSECONDS));
        }
    }
}
