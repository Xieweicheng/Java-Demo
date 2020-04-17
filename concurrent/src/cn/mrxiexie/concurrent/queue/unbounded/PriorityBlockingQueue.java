package cn.mrxiexie.concurrent.queue.unbounded;

import cn.mrxiexie.concurrent.basic.ThreadUtils;

/**
 * PriorityBlockingQueue是一个支持优先级的无界阻塞队列。默认情况下元素采取自然顺序升序排列。
 * 也可以自定义类实现compareTo()方法来指定元素排序规则，或者初始化PriorityBlockingQueue时，
 * 指定构造参数Comparator来对元素进行排序。需要注意的是不能保证同优先级元素的顺序。
 *
 * @author mrxiexie
 */
public class PriorityBlockingQueue {

    private static java.util.concurrent.PriorityBlockingQueue<Item> priorityBlockingQueue
            = new java.util.concurrent.PriorityBlockingQueue<>(4);

    public static void main(String[] args) {
        priorityBlockingQueue.add(new Item("Mc"));
        priorityBlockingQueue.add(new Item("Ma"));
        priorityBlockingQueue.add(new Item("Mb"));
        priorityBlockingQueue.add(new Item("Md"));
        ThreadUtils.log(priorityBlockingQueue.remove().getName());
        ThreadUtils.log(priorityBlockingQueue.remove().getName());
        ThreadUtils.log(priorityBlockingQueue.remove().getName());
        ThreadUtils.log(priorityBlockingQueue.remove().getName());
    }

    private static class Item implements Comparable<Item> {

        private String name;

        private Item(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(Item o) {
            return name.compareTo(o.name);
        }
    }
}
