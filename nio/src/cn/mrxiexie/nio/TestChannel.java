package cn.mrxiexie.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author mrxiexie
 * @date 8/12/19 1:43 PM
 */
public class TestChannel {

    public static void main(String[] args) throws Exception {
//        testFileChannel();
//        testServerSocketChannel();
//        testSocketChannel();
//        testScatter();
//        testGather();
        testTransform();
    }

    /**
     * 测试 FileChannel
     */
    private static void testFileChannel() throws Exception {
        File directory = new File("");
        FileChannel channel = new FileInputStream(new File(directory.getCanonicalPath() + "/nio/test.txt")).getChannel();
        ByteBuffer bb = ByteBuffer.allocate(4);
        while (channel.read(bb) > 0) {
            System.out.println(new String(bb.array()));
            bb.clear();
        }
        channel.close();
    }

    /**
     * 测试 serverSocketChannel 服务端
     */
    private static void testServerSocketChannel() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8088));
        System.out.println("等待客户端连接");
        SocketChannel accept = serverSocketChannel.accept();
        System.out.println("客户端连接");
        ByteBuffer bb = ByteBuffer.allocate(4);
        System.out.println(Arrays.toString(bb.array()));
        bb.clear();
        bb.putInt(1234567890);
        bb.clear();
        System.out.println("写入客户端");
        accept.write(bb);
        serverSocketChannel.close();
    }

    /**
     * 测试 socketChannel 客户端
     */
    private static void testSocketChannel() throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8088));
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(1);
        System.out.println(Arrays.toString(bb.array()));
        bb.clear();
        System.out.println("写入服务端");
        System.out.println(socketChannel.write(bb));
        bb.clear();
        socketChannel.read(bb);
        System.out.println(Arrays.toString(bb.array()));
        socketChannel.close();
    }

    /**
     * 测试分散
     */
    private static void testScatter() throws Exception {
        File directory = new File("");
        FileChannel channel = new FileInputStream(new File(directory.getCanonicalPath() + "/nio/test.txt")).getChannel();
        ByteBuffer bb1 = ByteBuffer.allocate((int) channel.size() / 2);
        ByteBuffer bb2 = ByteBuffer.allocate((int) channel.size() / 2);
        ByteBuffer[] bbs = {bb1, bb2};
        channel.read(bbs);
        System.out.println(new String(bb1.array()));
        System.out.println(new String(bb2.array()));
        channel.close();
    }

    /**
     * 测试聚集
     * <p>
     * 共享锁 所有共享锁持有者可读
     *     FileOutputStream.getChannel().tryLock(0L, Long.MAX_VALUE, true)获得共享锁， NonReadableChannelException异常，共享锁可读不可写，获取共享锁，必须要包含read channel 
     *     FileInputStream.getChannel().tryLock(0L, Long.MAX_VALUE, true)获得共享锁 可读不可写
     *     RandomAccessFile("r").getChannel().tryLock(0L, Long.MAX_VALUE, true)获得共享锁 可读不可写(拒绝写访问)
     *     RandomAccessFile("rw").getChannel().tryLock(0L, Long.MAX_VALUE, true)获得共享锁 可读不可写（写被锁【文件被占用】）
     * <p>
     * 独占锁 锁持有者可读可写，其他不持有锁者不可读不可写
     *     FileOutputStream.getChannel().tryLock()获得独占锁 可写不可读（不可读是因为该锁持有者无read channel）
     *     FileInputStream.getChannel().tryLock()获得独占锁 异常:NonWritableChannelException(独占锁必须可write)
     *     RandomAccessFile("rw").getChannel().tryLock()获得的独占锁 可写可读
     *     RandomAccessFile("r").getChannel().tryLock()获得的独占锁 NonWritableChannelException (独占锁必须可write)
     */
    private static void testGather() throws Exception {
        File directory = new File("");
        RandomAccessFile rw = new RandomAccessFile(directory.getCanonicalPath() + "/nio/testGather.txt", "rw");
        FileChannel channel = rw.getChannel();
        channel.tryLock();
        byte[] str1 = "Hi".getBytes();
        byte[] str2 = "Gather".getBytes();
        ByteBuffer bb1 = ByteBuffer.allocate(str1.length);
        ByteBuffer bb2 = ByteBuffer.allocate(str2.length);
        bb1.put(str1);
        bb2.put(str2);
        bb1.clear();
        bb2.clear();
        ByteBuffer[] bbs = {bb1, bb2};
        channel.write(bbs);
        channel.close();
    }

    /**
     * 测试传输
     */
    private static void testTransform() throws Exception {
        File directory = new File("");
        RandomAccessFile rw = new RandomAccessFile(directory.getCanonicalPath() + "/nio/testGather.txt", "rw");
        FileChannel fromChannel = new FileInputStream(new File(directory.getCanonicalPath() + "/nio/test.txt")).getChannel();
        FileChannel toChannel = rw.getChannel();
        toChannel.tryLock();
        fromChannel.transferTo(0, fromChannel.size(), toChannel);
        fromChannel.close();
        toChannel.close();
    }
}
