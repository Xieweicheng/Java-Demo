package cn.mrxiexie.nio;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.Arrays;

/**
 * @author mrxiexie
 * @date 8/14/19 2:25 PM
 */
public class TestPipe {

    public static void main(String[] args) throws Exception {
        testPipe();
    }

    private static void testPipe() throws Exception {
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sink = pipe.sink();
        ByteBuffer sinkbb = ByteBuffer.allocate(8);
        sinkbb.putLong(1234567890);
        sinkbb.flip();
        sink.write(sinkbb);
        System.out.println(Arrays.toString(sinkbb.array()));

        Pipe.SourceChannel source = pipe.source();
        ByteBuffer sourcebb = ByteBuffer.allocate(8);
        source.read(sourcebb);
        System.out.println(Arrays.toString(sourcebb.array()));
    }
}
