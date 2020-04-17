## 一、Java 并发编程基础

### 1. 线程简介

#### 线程优先级

通过 `setPriority(int)` 方法来修改优先级（1~10），默认优先级是5，优先级高的线程分配时间片的数量要多于优先级低的线程。设置线程优先级时，针对频繁阻塞（休眠或者I/O操作）的线程需要设置较高优先级，而偏重计算（需要较多CPU时间或者偏运算）的线程则设置较低的优先级，确保处理器不会被独占。在不同的JVM以及操作系统上，线程规划会存在差异，有些操作系统甚至会忽略对线程优先级的设定。

#### 线程的状态

| 状态名称     | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| NEW          | 初始状态，线程被构建，但是还没有调用 `start()` 方法          |
| RUNNABLE     | 运行状态，Java 线程将操作系统中的就绪和运行两种状态笼统地称作“运行中” |
| BLOCKED      | 阻塞状态，表示线程阻塞于锁                                   |
| WAITING      | 等待状态，表示线程进入等待状态，进入该状态表示当前线程需要等待其他线程做出一些特定动作（通知或中断） |
| TIME_WAITING | 超时等待状态，该状态不同于 WAITING，它是可以在指定的时间自行返回的 |
| TERMINATED   | 终止状态，表示当前线程已经执行完毕                           |

![image-20191212150300941](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20191212150300941.png)

注意：Java 将操作系统中的运行和就绪两个状态合并称为运行状态。阻塞状态是线程阻塞在进入 `synchronized` 关键字修饰的方法或代码块（获取锁）时的状态，但是阻塞在 `java.concurrent`包中Lock接口的线程状态却是等待状态，因为 `java.concurrent` 包中 Lock 接口对于阻塞的实现均使用了 `LockSupport` 类中的相关方法。

#### Daemon 线程

Daemon 线程是一种支持型线程，因为它主要被用作程序中后台调度以及支持性工作。这意味着，当一个 Java 虚拟机中不存在非 Daemon 线程的时候，Java虚拟机将会退出。可以通过调用 `Thread.setDaemon(true)`将线程设置为 Daemon 线程。

在构建 Daemon 线程时，不能依靠 finally 块中的内容来确保执行关闭或清理资源的逻辑。

### 2. 启动和终止线程

#### 理解中断

中断可以理解为线程的一个标识位属性，它表示一个运行中的线程是否被其他线程进行了中断操作。中断好比其他线程对该线程打了个招呼，其他线程通过调用该线程的interrupt()方法对其进行中断操作。

线程通过检查自身是否被中断来进行响应，线程通过方法isInterrupted()来进行判断是否被中断，也可以调用静态方法Thread.interrupted()对当前线程的中断标识位进行复位。如果该线程已经处于终结状态，即使该线程被中断过，在调用该线程对象的isInterrupted()时依旧会返回false。

从Java的API中可以看到，许多声明抛出InterruptedException的方法（例如Thread.sleep(long millis)方法）这些方法在抛出InterruptedException之前，Java虚拟机会先将该线程的中断标识位清除，然后抛出InterruptedException，此时调用isInterrupted()方法将会返回false。

#### 安全地终止线程

使用标志位或者中断去终止线程，如下标志位为 on

```
while (on && !Thread.currentThread().isInterrupted()){
	i++;
}
```

### 3. 线程间通讯

#### 等待/通知机制

| 方法名称          | 描述                                                         |
| ----------------- | ------------------------------------------------------------ |
| `notify()`        | 通知一个在对象上等待的线程，使其从 `wait()` 方法返回，而返回的前提是该线程获取到了对象的锁 |
| `notifyAll()`     | 通知所有等待在该对象的线程                                   |
| `wait()`          | 调用该方法的线程进入 WAITING 状态，只有等待另外的线程的通知或被中断才会返回，需要注意，调用 `wait()` 方法后，会释放对象的锁 |
| `wait(long)`      | 超时等待一段时间，这里的参数是毫秒，也就是等待长达 n 毫秒，如果没有通知就超时返回 |
| `wait(long, int)` | 对于超时时间更细粒度的控制，可以达到纳秒                     |

注意细节：

1）使用 `wait()`、`notify()` 和 `notifyAll()` 时需要先对调用对象加锁。

2）调用 `wait()` 方法后，线程状态由 RUNNING 变为 WAITING，并将当前线程放置到对象的等待队列。

3）`notify()` 或 `notifyAll()` 方法调用后，等待线程依旧不会从 `wait()` 返回，需要调用 `notify()` 或 `notifAll()` 的线程释放锁之后，等待线程才有机会从 `wait()` 返回。

4）`notify()` 方法将等待队列中的一个等待线程从等待队列中移到同步队列中，而 `notifyAll()` 方法则是将等待队列中所有的线程全部移到同步队列，被移动的线程状态由 WAITING 变为 BLOCKED。

5）从 `wait()` 方法返回的前提是获得了调用对象的锁。

![image-20191212165731020](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20191212165731020.png)

等待方遵循如下原则。

1）获取对象的锁。

2）如果条件不满足，那么调用对象的wait()方法，被通知后仍要检查条件。

3）条件满足则执行对应的逻辑。

```
synchronized(对象) {
       while(条件不满足) {
              对象.wait();
       }
       对应的处理逻辑
}
```

通知方遵循如下原则。

1）获得对象的锁。

2）改变条件。

3）通知所有等待在对象上的线程。

```
synchronized(对象) {
       改变条件
       对象.notifyAll();
}
```

#### Thread.join() 的使用

如果一个线程A执行了 `thread.join()` 语句，其含义是：当前线程A等待thread线程终止之后才从 `thread.join()` 返回。线程 Thread 除了提供 `join()` 方法之外，还提供了 `join(long millis)` 和 `join(long millis,int nanos)` 两个具备超时特性的方法。这两个超时方法表示，如果线程 thread 在给定的超时时间里没有终止，那么将会从该超时方法中返回。

## 二、Java 中的锁

### 1. Lock 接口

Lock 接口提供的 Synchronized 关键字不具备的主要特性

| 特性               | 描述                                                         |
| ------------------ | ------------------------------------------------------------ |
| 尝试非阻塞地获取锁 | 当前线程尝试获取锁，如果这一时刻锁没有被其他线程获取到，则成功获取并持有锁 |
| 能被中断地获取锁   | 与 synchronized 不同，获取到锁的线程能够响应中断，当获取到锁的线程被中断时，中断异常将会被抛出，同时锁会被释放 |
| 超时获取锁         | 在指定的截止时间之前获取锁，如果截止时间到了仍旧无法获取锁，则返回 |

Lock 的 Api

| 方法名称                                             | 描述                                                         |
| ---------------------------------------------------- | ------------------------------------------------------------ |
| `void lock()`                                        | 获取锁，调用该方法当前线程将会获取锁，当锁获得后，从该方法返回 |
| `void lockInterruptibly throws InterruptedException` | 可中断地获取锁，和 `lock()` 方法不同之处在于该方法会响应中断 |
| `boolean tryLock()`                                  | 尝试非阻塞的获取锁，调用该方法后立即返回，如果能获取则返回 true，否则返回 false |
| `boolean tryLock(long time, TimeUnit unit)`          | 超时的获取锁，当前线程在以下 3 种情况下会返回：<br />①当前线程在超时时间内获得了锁<br />②当前线程在超时时间内被终端<br />③超时时间结束，返回 false |
| `void unlock()`                                      | 释放锁                                                       |
| `Condition newCondition()`                           | 获取等待通知组件，该组件和当前的锁绑定，当前线程只有获得了锁，才能调用该组件的 `wait()` 方法，而调用后，当前线程将释放锁 |

### 2. 队列同步器（AQS）

学习同步器前先了解什么是同步器，什么是同步组件

队列同步器 `AbstractQueuedSynchronizer`（以下简称同步器）：是用来构建锁或者其他同步组件的基础框架，它使用了一个 int 成员变量表示同步状态，通过内置的FIFO队列来完成资源获取线程的排队工作。

同步组件：是指锁的实现 `ReentrantLock`、`ReentrantReadWriteLock` 和 `CountDownLatch` 等。

#### 队列同步器的接口与示例

同步器的设计是基于模板方法模式的，也就是说，使用者需要继承同步器并重写指定的方法，随后将同步器组合在自定义同步组件的实现中，并调用同步器提供的模板方法，而这些模板方法将会调用使用者重写的方法。

重写同步器指定的方法时，需要使用同步器提供的如下3个方法来访问或修改同步状态。

·`getState()`：获取当前同步状态。

·`setState(int newState)`：设置当前同步状态。

·`compareAndSetState(int expect,int update)`：使用 CAS 设置当前状态，该方法能够保证状态设置的原子性。

同步器可重写的方法：

| 方法名称                                      | 描述                                                         |
| --------------------------------------------- | ------------------------------------------------------------ |
| `protected boolean tryRequire(int arg)`       | 独占式获取同步状态，实现该方法需要查询当前状态并判断同步状态是否符合预期，然后再进行 CAS 设置同步状态 |
| `protected boolean tryRelease(int arg)`       | 独占式释放同步状态，等待获取同步状态的线程将有机会获取同步状态 |
| `protected int tryAcquireShared(int arg)`     | 共享式获取同步状态，返回大于等于 0 的值，表示获取成功，反之，获取失败 |
| `protected boolean tryReleaseShared(int arg)` | 共享式释放同步状态                                           |
| `protected boolean isHeldExclusively()`       | 当前同步器是否在独占模式下被线程占用，一般该方法表示是否被当前线程所占 |

实现自定义同步组件时，将会调用同步器提供的模板方法：

| 方法名称                                             | 描述                                                         |
| ---------------------------------------------------- | ------------------------------------------------------------ |
| `void acquire(int arg)`                              | 独占式获取同步状态，如果当前线程获取同步状态成功，则由该方法返回，否则，将会进入同步队列，该方法将会调用重写的 `tryAcquire(int arg)` 方法 |
| `void acquireInterruptibly(int arg)`                 | 与 `acquire(int arg)` 相同，但是该方法响应中断，当前线程未获取到同步状态而进入同步队列中，如果当前线程被中断，则该方法会抛出 `InterruptedException` 并返回 |
| `boolean tryAcquireNanos(int arg, long nanos)`       | 在 `acquireInterruptibly(int arg)` 基础上添加了超时限制，如果线程在超时时间内没有获取到同步状态，那么将会返回 false，如果获取到了返回 true |
| `void acquireShared(int arg)`                        | 共享式的获取同步状态，如果当前线程未获取到同步状态，将会进入同步队列等待，与独占式获取的主要区别是在同一时刻可以有多个线程获取到同步状态 |
| `void acquireSharedInterruptibly(int arg)`           | 与 `acquireShared(int arg)` 相同，该方法响应中断             |
| `boolean tryAcquireSharedNanos(int arg, long nanos)` | 在 `acquireShareInterruptibly(int arg)` 基础上增加了超时限制 |
| `boolean release(int arg)`                           | 独占式的释放同步状态，该方法会在释放同步状态后，将同步队列中第一个节点包含的线程唤醒 |
| `boolean releaseShared(int arg)`                     | 共享式的释放同步状态                                         |
| `Collection<Thread> getQueuedThreads()`              | 获取等待在同步状态上的线程集合                               |

同步器提供的模板方法基本上分为3类：独占式获取与释放同步状态、共享式获取与释放同步状态和查询同步队列中的等待线程情况。自定义同步组件将使用同步器提供的模板方法来实现自己的同步语义。

#### 队列同步器的实现分析

**同步队列**

同步器依赖内部的同步队列（一个FIFO双向队列）来完成同步状态的管理，当前线程获取同步状态失败时，同步器会将当前线程以及等待状态等信息构造成为一个节点（Node）并将其加入同步队列，同时会阻塞当前线程，当同步状态释放时，会把首节点中的线程唤醒，使其再次尝试获取同步状态。

同步队列中的节点（Node）用来保存获取同步状态失败的线程引用、等待状态以及前驱和后继节点，节点的属性类型与名称以及描述

![image-20191213150836167](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20191213150836167.png)

节点是构成同步队列的基础，同步器拥有首节点（head）和尾节点（tail），没有成功获取同步状态的线程将会成为节点加入该队列的尾部，同步队列的基本结构如下图所示。

![image-20191214095024860](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20191214095024860.png)

同步器包含了两个节点类型的引用，一个指向头节点，而另一个指向尾节点。试想一下，当一个线程成功地获取了同步状态（或者锁），其他线程将无法获取到同步状态，转而被构造成为节点并加入到同步队列中，而这个加入队列的过程必须要保证线程安全，因此同步器提供了一个基于CAS的设置尾节点的方法：compareAndSetTail(Node expect,Node update)，它需要传递当前线程“认为”的尾节点和当前节点，只有设置成功后，当前节点才正式与之前的尾节点建立关联。

同步器将节点加入到同步队列的过程如下图所示。

![image-20191214095058025](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20191214095058025.png)

同步队列遵循FIFO，首节点是获取同步状态成功的节点，首节点的线程在释放同步状态时，将会唤醒后继节点，而后继节点将会在获取同步状态成功时将自己设置为首节点，如下图所示。

![image-20191214095119737](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20191214095119737.png)

设置首节点是通过获取同步状态成功的线程来完成的，由于只有一个线程能够成功获取到同步状态，因此设置头节点的方法并不需要使用CAS来保证，它只需要将首节点设置成为原首节点的后继节点并断开原首节点的next引用即可。

以下三点实现分析请详细看书 5.2.2 小节

- 独占式同步状态获取与释放
- 共享式同步状态获取与释放
- 独占式超时获取同步状态

### 3. 重入锁

重入锁 ReentrantLock，顾名思义，就是支持重进入的锁，它表示该锁能够支持一个线程对资源的重复加锁。除此之外，该锁的还支持获取锁时的公平和非公平性选择。

重进入是指任意线程在获取到锁之后能够再次获取该锁而不会被锁所阻塞，该特性的实现需要解决以下两个问题：

1. **线程再次获取锁**，锁需要去识别获取锁的线程是否为当前占据锁的线程，如果是，则再次成功获取。
2. **锁的最终释放**，线程重复n次获取了锁，随后在第n次释放该锁后，其他线程能够获取到该锁。锁的最终释放要求锁对于获取进行计数自增，计数表示当前锁被重复获取的次数，而锁被释放时，计数自减，当计数等于0时表示锁已经成功释放。

公平与非公平获取锁的区别：公平性与否是针对获取锁而言的，如果一个锁是公平的，那么锁的获取顺序就应该符合请求的绝对时间顺序，也就是FIFO。

非公平性锁可能使线程“饥饿”，为什么它又被设定成默认的实现呢？

这是由于切换线程需要损耗资源，如果是公平锁，若同一个线程释放锁后又重新获取锁，这时它也得去同步队列尾部排队，这样就会频繁的发生线程的上下文切换，当线程越多，对CPU的损耗就会越严重。

### 4. 读写锁

ReentrantReadWriteLock，读写锁维护了一对锁，一个写锁一个读锁

一般情况下，读写锁的性能都会比排它锁好，因为大多数场景读是多于写的。在读多于写的情况下，读写锁能够提供比排它锁更好的并发性和吞吐量。读写锁在同一时刻可以允许多个读线程访问，但是在写线程访问时，所有的读线程和其他写线程均被阻塞。

![image-20200323145939430](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323145939430.png)

ReadWriteLock仅定义了获取读锁和写锁的两个方法，即readLock()方法和writeLock()方法，而其实现——ReentrantReadWriteLock，除了接口方法之外，还提供了一些便于外界监控其内部工作状态的方法：

![image-20200323150322252](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323150322252.png)

#### 读写锁的实现分析

**读写状态的设计**

读写锁同样依赖自定义同步器来实现同步功能，而读写状态就是其同步器的同步状态。要在一个整型变量上维护多种状态，就一定需要“按位切割使用”这个变量，读写锁将变量切分成了两个部分，高16位表示读，低16位表示写，划分方式如下图：

![image-20200323151004841](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323151004841.png)

当前同步状态表示一个线程已经获取了写锁，且重进入了两次，同时也连续获取了两次读锁。读写锁是如何迅速确定读和写各自的状态呢？

答案是通过位运算。假设当前同步状态值为S，写状态等于 `S&0x0000FFFF`（将高16位全部抹去），读状态等于 `S>>>16`（无符号补0右移16位）。当写状态增加1时，等于S+1，当读状态增加1时，等于 `S+(1<<16)`，也就是`S+0x00010000`。

根据状态的划分能得出一个推论：S不等于0时，当写状态（`S&0x0000FFFF`）等于0时，则读状态（`S>>>16`）大于0，即读锁已被获取。

**写锁的获取与释放**

写锁是一个支持重进入的排它锁。如果当前线程已经获取了写锁，则增加写状态。如果当前线程在获取写锁时，读锁已经被获取（读状态不为0）或者该线程不是已经获取写锁的线程，则当前线程进入等待状态

```java
protected final boolean tryAcquire(int acquires) {
    Thread current = Thread.currentThread();
    int c = getState();
    int w = exclusiveCount(c);
    if (c != 0) {
            // 存在读锁或者当前获取线程不是已经获取写锁的线程
            if (w == 0 || current != getExclusiveOwnerThread())
                    return false;
            if (w + exclusiveCount(acquires) > MAX_COUNT)
                    throw new Error("Maximum lock count exceeded");
            setState(c + acquires);
            return true;
    }
    if (writerShouldBlock() || !compareAndSetState(c, c + acquires)) {
            return false;
    }
    setExclusiveOwnerThread(current);
    return true;
}
```

该方法除了重入条件（当前线程为获取了写锁的线程）之外，增加了一个读锁是否存在的判断。如果存在读锁，则写锁不能被获取，原因在于：读写锁要确保写锁的操作对读锁可见，如果允许读锁在已被获取的情况下对写锁的获取，那么正在运行的其他读线程就无法感知到当前写线程的操作。因此，只有等待其他读线程都释放了读锁，写锁才能被当前线程获取，而写锁一旦被获取，则其他读写线程的后续访问均被阻塞。

写锁的释放与ReentrantLock的释放过程基本类似，每次释放均减少写状态，当写状态为0时表示写锁已被释放，从而等待的读写线程能够继续访问读写锁，同时前次写线程的修改对后续读写线程可见。

**读锁的获取与释放**

读锁是一个支持重进入的共享锁，它能够被多个线程同时获取，在没有其他写线程访问（或者写状态为0）时，读锁总会被成功地获取，而所做的也只是（线程安全的）增加读状态。如果当前线程已经获取了读锁，则增加读状态。如果当前线程在获取读锁时，写锁已被其他线程获取，则进入等待状态。获取读锁的实现从Java 5到Java 6变得复杂许多，主要原因是新增了一些功能，例如getReadHoldCount()方法，作用是返回当前线程获取读锁的次数。读状态是所有线程获取读锁次数的总和，而每个线程各自获取读锁的次数只能选择保存在ThreadLocal中，由线程自身维护，这使获取读锁的实现变得复杂。因此，这里将获取读锁的代码做了删减，保留必要的部分。

```java
protected final int tryAcquireShared(int unused) {
    for (;;) {
            int c = getState();
            int nextc = c + (1 << 16);
            if (nextc < c)
                    throw new Error("Maximum lock count exceeded");
            if (exclusiveCount(c) != 0 && owner != Thread.currentThread())
                    return -1;
            if (compareAndSetState(c, nextc)) 
                    return 1;
    }
}
```

在tryAcquireShared(int unused)方法中，如果其他线程已经获取了写锁，则当前线程获取读锁失败，进入等待状态。如果当前线程获取了写锁或者写锁未被获取，则当前线程（线程安全，依靠CAS保证）增加读状态，成功获取读锁。

读锁的每次释放（线程安全的，可能有多个读线程同时释放读锁）均减少读状态，减少的值是（1<<16）。

**锁降级**

锁降级指的是写锁降级成为读锁。如果当前线程拥有写锁，然后将其释放，最后再获取读锁，这种分段完成的过程不能称之为锁降级。锁降级是指把持住（当前拥有的）写锁，再获取到读锁，随后释放（先前拥有的）写锁的过程。

具体示例看代码示例 `ReadWriteLock.CachedData`

### 5. LockSupport工具

Lock 接口使用 LockSupport 的一组公共静态方法来实现线程的阻塞唤醒功能，而 LockSupport 也成为构建同步组件的基础工具。

![image-20200323152712791](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323152712791.png)

在Java 6中，LockSupport增加了park(Object blocker)、parkNanos(Object blocker,long nanos)和parkUntil(Object blocker,long deadline)3个方法，用于实现阻塞当前线程的功能，其中参数blocker是用来标识当前线程在等待的对象，该对象主要用于问题排查和系统监控。

### 6. Condition接口

任意一个Java对象，都拥有一组监视器方法（定义在java.lang.Object上），主要包括wait()、wait(long timeout)、notify()以及notifyAll()方法，这些方法与synchronized同步关键字配合，可以实现等待/通知模式。Condition接口也提供了类似Object的监视器方法，与Lock配合可以实现等待/通知模式，但是这两者在使用方式以及功能特性上还是有差别的。

![image-20200323153008170](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323153008170.png)

Condition定义了等待/通知两种类型的方法，当前线程调用这些方法时，需要提前获取到Condition对象关联的锁。Condition对象是由Lock对象（调用Lock对象的newCondition()方法）创建出来的，换句话说，Condition是依赖Lock对象的。

![image-20200323153442204](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323153442204.png)

![image-20200323153446949](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323153446949.png)

#### Condition的实现分析

ConditionObject是同步器AbstractQueuedSynchronizer的内部类，因为Condition的操作需要获取相关联的锁，所以作为同步器的内部类也较为合理。

每个Condition对象都包含着一个队列（以下称为等待队列），该队列是Condition对象实现等待/通知功能的关键。

下面将分析Condition的实现，主要包括：等待队列、等待和通知，下面提到的Condition如果不加说明均指的是ConditionObject。

**等待队列**

等待队列是一个FIFO的队列，在队列中的每个节点都包含了一个线程引用，该线程就是在Condition对象上等待的线程，如果一个线程调用了Condition.await()方法，那么该线程将会释放锁、构造成节点加入等待队列并进入等待状态。事实上，节点的定义复用了同步器中节点的定义，也就是说，同步队列和等待队列中节点类型都是同步器的静态内部类AbstractQueuedSynchronizer.Node。

一个Condition包含一个等待队列，Condition拥有首节点（firstWaiter）和尾节点（lastWaiter）。当前线程调用Condition.await()方法，将会以当前线程构造节点，并将节点从尾部加入等待队列

![image-20200323154342632](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323154342632.png)

Condition拥有首尾节点的引用，而新增节点只需要将原有的尾节点nextWaiter指向它，并且更新尾节点即可。上述节点引用更新的过程并没有使用CAS保证，原因在于调用await()方法的线程必定是获取了锁的线程，也就是说该过程是由锁来保证线程安全的。

在Object的监视器模型上，一个对象拥有一个同步队列和等待队列，而并发包中的Lock（更确切地说是同步器）拥有一个同步队列和多个等待队列

![image-20200323155413931](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323155413931.png)

Condition的实现是同步器的内部类，因此每个Condition实例都能够访问同步器提供的方法，相当于每个Condition都拥有所属同步器的引用

**等待**

调用Condition的await()方法（或者以await开头的方法），会使当前线程进入等待队列并释放锁，同时线程状态变为等待状态。当从await()方法返回时，当前线程一定获取了Condition相关联的锁。

如果从队列（同步队列和等待队列）的角度看await()方法，当调用await()方法时，相当于同步队列的首节点（获取了锁的节点）移动到Condition的等待队列中。

```java
public final void await() throws InterruptedException {
    if (Thread.interrupted())
            throw new InterruptedException();
    // 当前线程加入等待队列
    Node node = addConditionWaiter();
    // 释放同步状态，也就是释放锁
    int savedState = fullyRelease(node);
    int interruptMode = 0;
    while (!isOnSyncQueue(node)) {
            LockSupport.park(this);
            if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
    }
    if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
            interruptMode = REINTERRUPT;
    if (node.nextWaiter != null)
            unlinkCancelledWaiters();
    if (interruptMode != 0)
            reportInterruptAfterWait(interruptMode);
}
```

调用该方法的线程成功获取了锁的线程，也就是同步队列中的首节点，该方法会将当前线程构造成节点并加入等待队列中，然后释放同步状态，唤醒同步队列中的后继节点，然后当前线程会进入等待状态。

当等待队列中的节点被唤醒，则唤醒节点的线程开始尝试获取同步状态。如果不是通过其他线程调用Condition.signal()方法唤醒，而是对等待线程进行中断，则会抛出InterruptedException。

如果从队列的角度去看，当前线程加入Condition的等待队列，该过程如图5-11示。

如图所示，同步队列的首节点并不会直接加入等待队列，而是通过addConditionWaiter()方法把当前线程构造成一个新的节点并将其加入等待队列中。

**通知**

调用Condition的signal()方法，将会唤醒在等待队列中等待时间最长的节点（首节点），在唤醒节点之前，会将节点移到同步队列中。

![image-20200323161143662](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323161143662.png)

```java
public final void signal() {
    if (!isHeldExclusively())
            throw new IllegalMonitorStateException();
    Node first = firstWaiter;
    if (first != null)
            doSignal(first);
}
```

调用该方法的前置条件是当前线程必须获取了锁，可以看到signal()方法进行了isHeldExclusively()检查，也就是当前线程必须是获取了锁的线程。接着获取等待队列的首节点，将其移动到同步队列并使用LockSupport唤醒节点中的线程

![image-20200323161209504](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323161209504.png)

通过调用同步器的enq(Node node)方法，等待队列中的头节点线程安全地移动到同步队列。当节点移动到同步队列后，当前线程再使用LockSupport唤醒该节点的线程。

被唤醒后的线程，将从await()方法中的while循环中退出（isOnSyncQueue(Node node)方法返回true，节点已经在同步队列中），进而调用同步器的acquireQueued()方法加入到获取同步状态的竞争中。

成功获取同步状态（或者说锁）之后，被唤醒的线程将从先前调用的await()方法返回，此时该线程已经成功地获取了锁。

Condition的signalAll()方法，相当于对等待队列中的每个节点均执行一次signal()方法，效果就是将等待队列中所有节点全部移动到同步队列中，并唤醒每个节点的线程。

## 三、Java 并发容器和框架

### ConcurrentHashMap

略

### ConcurrectLinkedQueue

在并发编程中，有时候需要使用线程安全的队列。如果要实现一个线程安全的队列有两种方式：

- 一种是使用阻塞算法
- 另一种是使用非阻塞算法。使用阻塞算法的队列可以用一个锁（入队和出队用同一把锁）或两个锁（入队和出队用不同的锁）等方式来实现。非阻塞的实现方式则可以使用循环CAS的方式来实现。

本节让我们一起来研究一下Doug Lea是如何使用非阻塞的方式来实现线程安全队列ConcurrentLinkedQueue的，相信从大师身上我们能学到不少并发编程的技巧。

ConcurrentLinkedQueue是一个基于链接节点的无界线程安全队列，它采用先进先出的规则对节点进行排序，当我们添加一个元素的时候，它会添加到队列的尾部；当我们获取一个元素时，它会返回队列头部的元素。它采用了“wait-free”算法（即CAS算法）来实现，该算法在Michael&Scott算法上进行了一些修改。

### Java 中的阻塞队列

阻塞队列（BlockingQueue）是一个支持两个附加操作的队列。这两个附加的操作支持阻塞的插入和移除方法。

- 支持阻塞的插入方法：意思是当队列满时，队列会阻塞插入元素的线程，直到队列不满。
- 支持阻塞的移除方法：意思是在队列为空时，获取元素的线程会等待队列变为非空。

阻塞队列常用于生产者和消费者的场景，生产者是向队列里添加元素的线程，消费者是从队列里取元素的线程。阻塞队列就是生产者用来存放元素、消费者用来获取元素的容器。

在阻塞队列不可用时，这两个附加操作提供了4种处理方式

![image-20200323161825028](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323161825028.png)

- 抛出异常：当队列满时，如果再往队列里插入元素，会抛出IllegalStateException（"Queue full"）异常。当队列空时，从队列里获取元素会抛出NoSuchElementException异常。
- 返回特殊值：当往队列插入元素时，会返回元素是否插入成功，成功返回true。如果是移除方法，则是从队列里取出一个元素，如果没有则返回null。
- 一直阻塞：当阻塞队列满时，如果生产者线程往队列里put元素，队列会一直阻塞生产者线程，直到队列可用或者响应中断退出。当队列空时，如果消费者线程从队列里take元素，队列会阻塞住消费者线程，直到队列不为空。
- 超时退出：当阻塞队列满时，如果生产者线程往队列里插入元素，队列会阻塞生产者线程一段时间，如果超过了指定的时间，生产者线程就会退出。

这两个附加操作的4种处理方式不方便记忆，所以我找了一下这几个方法的规律。put和take分别尾首含有字母t，offer和poll都含有字母o。

| Queue                 | 是否有界 | 描述                                 |
| --------------------- | -------- | ------------------------------------ |
| ArrayBlockingQueue    | 是       | 一个由数组结构组成的有界阻塞队列     |
| LinkedBlockingQueue   | 是       | 一个由链表结构组成的有界阻塞队列     |
| PriorityBlockingQueue | 否       | 一个支持优先级排序的无界阻塞队列     |
| DelayQueue            | 否       | 一个使用优先级队列实现的无界阻塞队列 |
| SynchronousQueue      | 是       | 一个不存储元素的阻塞队列             |
| LinkedTransferQueue   | 否       | 一个由链表结构组成的无界阻塞队列     |
| LinkedBlockingDeque   | 是       | 一个由链表结构组成的双向阻塞队列     |

**ArrayBlockingQueue**

ArrayBlockingQueue是一个用数组实现的有界阻塞队列。此队列按照先进先出（FIFO）的原则对元素进行排序。

**LinkedBlockingQueue**

LinkedBlockingQueue是一个用链表实现的有界阻塞队列。此队列的默认和最大长度为Integer.MAX_VALUE。此队列按照先进先出的原则对元素进行排序。

**PriorityBlockingQueue**

PriorityBlockingQueue是一个支持优先级的无界阻塞队列。默认情况下元素采取自然顺序升序排列。也可以自定义类实现compareTo()方法来指定元素排序规则，或者初始化PriorityBlockingQueue时，指定构造参数Comparator来对元素进行排序。需要注意的是不能保证同优先级元素的顺序。

**DelayQueue**

DelayQueue是一个支持延时获取元素的无界阻塞队列。队列使用PriorityQueue来实现。队列中的元素必须实现Delayed接口，在创建元素时可以指定多久才能从队列中获取当前元素。只有在延迟期满时才能从队列中提取元素。

DelayQueue非常有用，可以将DelayQueue运用在以下应用场景。

- 缓存系统的设计：可以用DelayQueue保存缓存元素的有效期，使用一个线程循环查询DelayQueue，一旦能从DelayQueue中获取元素时，表示缓存有效期到了。
- 定时任务调度：使用DelayQueue保存当天将会执行的任务和执行时间，一旦从DelayQueue中获取到任务就开始执行，比如TimerQueue就是使用DelayQueue实现的。

**SynchronousQueue**

SynchronousQueue是一个不存储元素的阻塞队列。每一个put操作必须等待一个take操作，否则不能继续添加元素。

**LinkedTransferQueue**

LinkedTransferQueue是一个由链表结构组成的无界阻塞TransferQueue队列。相对于其他阻塞队列，LinkedTransferQueue多了tryTransfer和transfer方法。

- transfer方法：如果当前有消费者正在等待接收元素（消费者使用take()方法或带时间限制的poll()方法时），transfer方法可以把生产者传入的元素立刻transfer（传输）给消费者。如果没有消费者在等待接收元素，transfer方法会将元素存放在队列的tail节点，并等到该元素被消费者消费了才返回。
- tryTransfer方法：tryTransfer方法是用来试探生产者传入的元素是否能直接传给消费者。如果没有消费者等待接收元素，则返回false。和transfer方法的区别是tryTransfer方法无论消费者是否接收，方法立即返回，而transfer方法是必须等到消费者消费了才返回。

**LinkedBlockingDeque**

LinkedBlockingDeque是一个由链表结构组成的双向阻塞队列。所谓双向队列指的是可以从队列的两端插入和移出元素。双向队列因为多了一个操作队列的入口，在多线程同时入队时，也就减少了一半的竞争。相比其他的阻塞队列，LinkedBlockingDeque多了addFirst、addLast、offerFirst、offerLast、peekFirst和peekLast等方法，以First单词结尾的方法，表示插入、获取（peek）或移除双端队列的第一个元素。以Last单词结尾的方法，表示插入、获取或移除双端队列的最后一个元素。另外，插入方法add等同于addLast，移除方法remove等效于removeFirst。但是take方法却等同于takeFirst，不知道是不是JDK的bug，使用时还是用带有First和Last后缀的方法更清楚。

在初始化LinkedBlockingDeque时可以设置容量防止其过度膨胀。另外，双向阻塞队列可以运用在“工作窃取”模式中

#### 阻塞队列的实现原理

==TODO==

### Fork/Join 框架

#### 什么是Fork/Join框架

Fork/Join框架是Java 7提供的一个用于并行执行任务的框架，是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架。

我们再通过Fork和Join这两个单词来理解一下Fork/Join框架。Fork就是把一个大任务切分为若干子任务并行的执行，Join就是合并这些子任务的执行结果，最后得到这个大任务的结果。比如计算1+2+…+10000，可以分割成10个子任务，每个子任务分别对1000个数进行求和，最终汇总这10个子任务的结果。

![image-20200323163057185](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323163057185.png)

#### 工作窃取算法

工作窃取（work-stealing）算法是指某个线程从其他队列里窃取任务来执行。那么，为什么需要使用工作窃取算法呢？假如我们需要做一个比较大的任务，可以把这个任务分割为若干互不依赖的子任务，为了减少线程间的竞争，把这些子任务分别放到不同的队列里，并为每个队列创建一个单独的线程来执行队列里的任务，线程和队列一一对应。比如A线程负责处理A队列里的任务。但是，有的线程会先把自己队列里的任务干完，而其他线程对应的队列里还有任务等待处理。干完活的线程与其等着，不如去帮其他线程干活，于是它就去其他线程的队列里窃取一个任务来执行。而在这时它们会访问同一个队列，所以为了减少窃取任务线程和被窃取任务线程之间的竞争，通常会使用双端队列，被窃取任务线程永远从双端队列的头部拿任务执行，而窃取任务的线程永远从双端队列的尾部拿任务执行。

![image-20200323163247852](/home/mrxiexie/Mine/project/IdeaProjects/Java-Demo/concurrent/Thread.assets/image-20200323163247852.png)

- 工作窃取算法的优点：充分利用线程进行并行计算，减少了线程间的竞争。
- 工作窃取算法的缺点：在某些情况下还是存在竞争，比如双端队列里只有一个任务时。并且该算法会消耗了更多的系统资源，比如创建多个线程和多个双端队列。

==TODO==



## 常见错误

### 使用 `notify()` 而不是 `notifyAll()`

使用 `notify()` 会随机从**等待队列**中抽取一个线程到**同步队列**中，而 `notifyAll()` 则会把所有**等待线程**中的线程移动**同步队列**中，若只是使用 `notify()`，可能会导致所有线程被挂起而无法继续工作，以下栗子：

比如说，你是你家挣钱的，儿子和女儿是花钱的。儿子给家里要100，女儿要30。可是家里没钱，他们只能等。后来你出去打工，赚钱了，赚了50，这时你要在儿子和女儿之间选择一个人叫醒。

如果不凑巧，你把儿子叫醒了，儿子发现钱还是不够，又去等。因为你只能叫一次，女儿就错过了使用这50块钱的机会。所以，你决定把所有的人都叫醒，虽然费劲一点。这样一来，儿子发现不够，接着等，女儿发现够了，就用了。