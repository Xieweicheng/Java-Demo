java nio(非阻塞 io)

### 概念

`Buffer`：缓冲区，用于存放数据

`Channel`：管道，表示打开到 IO 设备的连接，用于传输

`Selector`：选择器

数据是从通道读入缓冲区，从缓冲区写入通道中的。

### 一、Buffer

类似于数组，可以存放多种类型的数据，根据数据的不同有以下常用子类：

- ByteBuffer
- CharBuffer
- ShortBuffer
- IntBuffer
- LongBuffer
- FloatBuffer
- DoubleBuffer

#### 概念

capacity：buffer 容量

limit：限制，位于 limit 后的数据无法读取或写入

position：位置，下一个要读取或写入的数据索引

mark 和 reset：标记和重置，标记是一个索引，通过 buffer 中的 mark() 方法指定 buffer 中一个特定的 position，之后可以通过调用 reset() 方法恢复到这个 position

#### 常用方法

| 方法                            | 描述                                                         |
| ------------------------------- | ------------------------------------------------------------ |
| `Buffer allocate(int capacity)` | 分配多大容量的 buffer，不能为负，且设置后无法修改            |
| `void put(byte)`                | 将单个字节写入 buffer 的当前位置                             |
| `byte get()`                    | 读取单个字节                                                 |
| `Buffer clear()`                | 清空缓冲区                                                   |
| `Buffer flip()`                 | 将缓冲区的 limit 设置在当前 position，并把 position 设置为 0 |
| `int capacity()`                | 返回 buffer 的 capacity                                      |
| `int remaining()`               | limit - position，剩余个数                                   |
| `boolean hasRemaining()`        | 是否还有元素                                                 |
| `int limit()`                   | 返回 limit                                                   |
| `Buffer limit(int n)`           | 设置 limit                                                   |
| `int position()`                | 返回 position                                                |
| `Buffer position()`             | 设置 position                                                |
| `Buffer mark()`                 | 标记当前 position                                            |
| `Buffer reset()`                | 将 position 切换到之前 mark 的位置                           |
| `Buffer rewind()`               | 将 position 设置为 0，并且取消 mark                           |
| `Buffer duplicate()`            | 创建 buffer 的副本，修改原 buffer 中的数据，副本 buffer 中的数据也会修改，反之亦然。position，limit，mark 则相互独立。 |
| `Buffer order()`                | 修改 buffer 的字节顺序                                       |
| `Buffer slice()`                | 分片                                                         |
| `Buffer compact()`              | 压缩 buffer                                                  |

#### 直接与非直接缓冲区

字节缓冲区要么是直接的，要么是非直接的。如果为直接字节缓冲区，则 Java 虚拟机会尽最大努力直接在
此缓冲区上执行本机 I/O 操作。也就是说，在每次调用基础操作系统的一个本机 I/O 操作之前(或之后)，
虚拟机都会尽量避免将缓冲区的内容复制到中间缓冲区中(或从中间缓冲区中复制内容)。

直接字节缓冲区可以通过调用此类的 allocateDirect() 工厂方法来创建。此方法返回的缓冲区进行分配和取消
分配所需成本通常高于非直接缓冲区。直接缓冲区的内容可以驻留在常规的垃圾回收堆之外，因此，它们对
应用程序的内存需求量造成的影响可能并不明显。所以，建议将直接缓冲区主要分配给那些易受基础系统的
本机 I/O 操作影响的大型、持久的缓冲区。一般情况下，最好仅在直接缓冲区能在程序性能方面带来明显好
处时分配它们。

直接字节缓冲区还可以通过 FileChannel 的 map() 方法 将文件区域直接映射到内存中来创建。该方法返回
MappedByteBuffer 。Java 平台的实现有助于通过 JNI 从本机代码创建直接字节缓冲区。如果以上这些缓冲区
中的某个缓冲区实例指的是不可访问的内存区域，则试图访问该区域不会更改该缓冲区的内容，并且将会在
访问期间或稍后的某个时间导致抛出不确定的异常。

字节缓冲区是直接缓冲区还是非直接缓冲区可通过调用其 isDirect() 方法来确定。提供此方法是为了能够在
性能关键型代码中执行显式缓冲区管理。

### 二、Channel

`Channel` 是 `java.nio.channels` 的接口，类似与传统的“流”，只不过 `Channel` 本身不能访问数据，只能与 `Buffer` 进行交互。

#### 常用 Channel

- FileChannel:用于读取、写入、映射和操作文件的通道。
- DatagramChannel:通过 UDP 读写网络中的数据通道。
- SocketChannel:通过 TCP 读写网络中的数据。
- ServerSocketChannel:可以监听新进来的 TCP 连接，对每一个新进来的连接都会创建一个 SocketChannel。

#### 获取 Channel 的常用方法

以下类对应的对象可以通过 `getChannel` 获取对应的 `channel`

- FileInputStream
- FileOutputStream
- RandomAccessFile
- DatagramSocket
- Socket
- ServerSocket

`Files.newByteChannel()`或者对应 `channel` 的 `open` 方法

#### Channel 数据传输

通过 `channel` 的 `write(buffer)` 方法写入，`read(buffer)` 方法读取 

#### 分散和聚集

**Scatter：**是指从 Channel 中读取的数据”分散“到多个 Buffer 中

**Gather：**是指将多个 Buffer 中的数据”聚集“到 Channel

### 三、Selector

> 选择器(Selector) 是 SelectableChannle 对象的多路复用器，Selector 可以同时监控多个 SelectableChannel 的 IO 状况，也就是说，利用 Selector可使一个单独的线程管理多个 Channel。Selector 是非阻塞 IO 的核心。

创建 Selector：`Selector selector = Selector.open()`

注册 channel 到 selector 中：`SelectableChannel.register(Selector sel， int ops)`

#### SelectionKey

> 表示 SelectableChannel 和 Selector 之间的注册关系。每次向选择器注册通道时就会选择一个事件(选择键)。选择键包含两个表示为整数值的操作集。操作集的每一位都表示该键的通道所支持的一类可选择操作

当调用 register(Selector sel， int ops) 将通道注册选择器时，选择器对通道的监听事件，需要通过第二个参数 ops 指定

可以监听的事件类型(可使用 SelectionKey 的四个常量表示)：

- 读：`SelectionKey.OP_READ `

- 写：`SelectionKey.OP_WRITE`

- 连接：`SelectionKey.OP_CONNECT`

- 接收：`SelectionKey.OP_ACCEPT `

若注册时不止监听一个事件，则可以使用“位或”操作符连接

##### 常用方法：

| 方法                          | 描述                             |
| ----------------------------- | -------------------------------- |
| `int interestOps()`           | 获取感兴趣事件集合               |
| `void interestOps()`          | 设置感兴趣事件集合               |
| `int readyOps()`              | 获取通道已经准备就绪的操作的集合 |
| `SelectableChannel channel()` | 获取注册通道                     |
| `Selector selector()`         | 返回选择器                       |
| `boolean isReadable()`        | 检测 Channal 中读事件是否就绪    |
| `boolean isWritable()`        | 检测 Channal 中写事件是否就绪    |
| `boolean isConnectable()`     | 检测 Channel 中连接是否就绪      |
| `boolean isAcceptable()`      | 检测 Channel 中接收是否就绪      |

#### 常用方法

| 方法 | 描述 |
| ---- | ---- |
| `Set<SelectionKey> keys()`         | 所有的 SelectionKey 集合。代表注册在该Selector上的Channel    |
| `Set<SelectionKey> selectedKeys()` | 被选择的 SelectionKey 集合。返回此Selector的已选择键集       |
| `int select()`                     | 监控所有注册的Channel,当它们中间有需要处理的 IO 操作时,该方法返回,并将对应得的 SelectionKey 加入被选择的 SelectionKey 集合中,该方法返回这些 Channel 的数量。 |
| `int select(long timeout)`         | 可以设置超时时长的 select() 操作                             |
| `int selectNow()`                  | 执行一个立即返回的 select() 操作,该方法不会阻塞线程          |
| `Selector wakeup()`                | 使一个还未返回的 select() 方法立即返回                       |
| `void close()`                     | 关闭该选择器                                                 |

### 四、Pipe

>  Java NIO 管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。数据会被写到sink通道,从source通道读取。

创建管道：`Pipe pipe = Pipe.open()`

写入管道：`pipe.sink().write(buffer)`

读取管道：`pipe.source().read(buffer)`