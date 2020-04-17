package cn.mrxiexie.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author mrxiexie
 * @date 8/13/19 3:56 PM
 */
public class TestSelector {

    private static CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
    private static CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

    public static void main(String[] args) throws Exception {
//        testServerSocketChannel();
        testSocketChannel();
    }

    /**
     * 服务端
     */
    private static void testServerSocketChannel() throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 开启非阻塞
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8088));
        // 注册 channel 到 selector 中，并注册 accept 为兴趣
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.accept();

        while (true) {
            Thread.sleep(1000);
            // 选择一组键，其对应的 channel 已准备好进行 I/O 操作
            selector.select();
            Iterator iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    // 客户端连接后，返回注册了 accept 的 channel
                    System.out.println("客户端连接");
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel channel = serverChannel.accept();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer bb = ByteBuffer.allocate(1024);
                    bb.clear();
                    if (channel.read(bb) > 0) {
                        // 切换到读模式
                        bb.flip();
                        // 读取 position 到 limit 之间的值
                        CharBuffer decode = decoder.decode(bb);
                        String content = decode.toString();
                        System.out.println("收到信息：" + content);
                        String replace = content.replace("你", "我").replace("？", "！").replace("吗", "啊");
                        channel.write(ByteBuffer.wrap(replace.getBytes()));
                    } else {
                        // channel.read 返回 -1 时，说明客户端关闭了连接，若不关闭连接，会一直返回 -1
                        channel.close();
                    }
                }
            }
        }
    }

    /**
     * 客户端
     */
    private static void testSocketChannel() throws Exception {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(8088));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        bb:
        while (true) {
            Thread.sleep(1000);
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isConnectable()) {
                    // 返回注册了 connect 的 channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // isConnectionPending 判断通道是否正在进行操作连接.只有当尚未 finishConnection 且已经调用 connect 时,返回 true.
                    if (channel.isConnectionPending()) {
                        // 非阻塞模式下，connect 后需要调用 finishConnect 完成真正的连接
                        if (channel.finishConnect()) {
                            System.out.println("channel.isConnected");
                            // 设置该 key 的兴趣为 read
                            key.interestOps(SelectionKey.OP_READ);
                            channel.write(ByteBuffer.wrap(getMsgFromConsole().getBytes()));
                        } else {
                            key.cancel();
                        }
                    }
                } else if (key.isReadable()) {
                    ByteBuffer bb = ByteBuffer.allocate(1024);
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.read(bb);
                    bb.flip();
                    System.out.println("收到信息：" + decoder.decode(bb).toString());
                    String content = getMsgFromConsole();
                    channel.write(ByteBuffer.wrap(content.getBytes()));
                    if ("bye".equals(content)) {
                        channel.close();
                        break bb;
                    }
                }
            }
        }
    }

    /**
     * 从控制台中获取消息
     */
    private static String getMsgFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要发送的信息：");
        return scanner.next();
    }
}