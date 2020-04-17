package cn.mrxiexie.nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * @author mrxiexie
 * @date 8/10/19 3:20 PM
 */
public class TestBuffer {

    public static void main(String[] args) {
//        testBuffer();
//        testDuplicate();
//        testCompact();
//        testSlice();
        testOrder();
    }

    private static void testBuffer() {
        ByteBuffer bb = ByteBuffer.allocate(29);
        bb
                // 1
                .put((byte) 1)
                // 2
                .putChar('a')
                // 2
                .putShort((short) 1)
                // 4
                .putInt(1)
                // 8
                .putLong(1L)
                // 4
                .putFloat(1)
                // 8
                .putDouble(1);
        System.out.println(Arrays.toString(bb.array()));
        System.out.println("capacity : " + bb.capacity());
        System.out.println("position : " + bb.position());
        System.out.println("limit : " + bb.limit());
        System.out.println("remaining = limit - position : " + bb.remaining());
    }

    /**
     * 创建 buffer 的副本
     * <p>
     * 修改原 buffer 中的数据，副本 buffer 中的数据也会修改，反之亦然。
     * position，limit，mark 则相互独立。
     */
    private static void testDuplicate() {
        System.out.println("========= 测试 duplicate 方法 =========");
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(123456);
        System.out.println("原来 buffer : " + Arrays.toString(bb.array()) + " " + bb);
        ByteBuffer duplicate = bb.duplicate();
        System.out.println("修改副本 position 为 0，并设置 position 为 0 的值为 100");
        duplicate.position(0);
        duplicate.put((byte) 100);
        System.out.println("原来 buffer : " + Arrays.toString(bb.array()) + " " + bb);
        System.out.println("副本 buffer : " + Arrays.toString(duplicate.array()) + " " + duplicate);
    }

    /**
     * 压缩 buffer
     * <p>
     * 将缓冲区 position 到 limit 的数据复制到，position 为 0 到 n = limit - position - 1
     * 并将 position 设为 n + 1，limit 设为 capacity
     * 栗子：p = 5、limit = 7，则复制到 p = 0、p = 1 的位置
     * <p>
     * 将缓冲区的位置设置为复制的字节数，而不是零，以便调用此方法后可以紧接着调用另一个相对 put 方法。
     * <p>
     * 从缓冲区写入数据之后调用此方法，以防写入不完整。例如，以下循环语句通过 buf 缓冲区将字节从一个 channel 复制到另一个 channel：
     * <blockquote><pre>{@code
     *   buf.clear();          // Prepare buffer for use
     *   while (in.read(buf) >= 0 || buf.position != 0) {
     *       buf.flip();
     *       out.write(buf);
     *       buf.compact();    // In case of partial write
     *   }
     * }</pre></blockquote>
     */
    private static void testCompact() {
        System.out.println("========= 测试 compact 方法 =========");
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(1234567890L);
        System.out.println(Arrays.toString(bb.array()));
        bb.position(2);
        bb.limit(6);
        bb.compact();
        System.out.println(Arrays.toString(bb.array()));
        System.out.println(bb);
    }

    /**
     * 分片
     * <p>
     * 将原 buffer 分片，新的 buffer 的数据是 原 buffer position 后面的数据，他们的 position，limit，capacity相互独立
     * 由于原 buffer 部分数据和 新 buffer 共享，修改共享数据将会同步共享。
     */
    private static void testSlice() {
        System.out.println("========= 测试 slice 方法 =========");
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(123456789);
        System.out.println(Arrays.toString(bb.array()));
        bb.position(2);
        ByteBuffer slice = bb.slice();
        System.out.println(slice);
        slice.put((byte) 1);
        slice.put((byte) 1);
        System.out.println(Arrays.toString(slice.array()));
        System.out.println(slice);
        System.out.println(Arrays.toString(bb.array()));
        System.out.println(bb);
    }

    /**
     * 修改 buffer 的字节顺序
     *
     * {@link ByteOrder#BIG_ENDIAN} 大端字节序，人类习惯的读写顺序
     * {@link ByteOrder#LITTLE_ENDIAN} 小端字节序，计算机习惯的读写顺序
     */
    private static void testOrder() {
        System.out.println("========= 测试 order 方法 =========");
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(123456789)
                .order(ByteOrder.BIG_ENDIAN)
                .putInt(123456789);
        System.out.println(Arrays.toString(bb.array()));
    }
}
