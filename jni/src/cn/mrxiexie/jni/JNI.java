package cn.mrxiexie.jni;

import java.util.Arrays;
import java.util.Random;

/**
 * 1、build 一下项目，或者直接 run，此时会报错，但是生成了 class 文件
 * 2、打开 Terminal 命令行，输入
 * javah -jni -classpath jni/out/production/jni/ -d ./jni/jni cn.mrxiexie.jni.JNI
 * 生成 .h 文件
 * 3、根据生成的 .h 文件，写对应的 c 文件 JNI.c
 * 4、打开 Terminal 命令行，输入
 * gcc -I /lib64/jvm/java-8-jdk/include -I /lib64/jvm/java-8-jdk/include/linux -fPIC -c ./jni/jni/JNI.c -o ./jni/jni/JNI.o
 * 生成 .o 文件
 * 5、打开 Terminal 命令行，输入
 * gcc -shared -o ./jni/jni/libJNI.so ./jni/jni/JNI.o
 * 生成 .so 文件
 * 6、把 .so 文件放在 lib 包中，project structure 对应的 module 引入 dependences
 * 7、运行即可
 * <p>
 * <p>
 * c 端调用 java 端方法，需要方法的名字和签名信息，签名信息格式为 (parameters)return-type
 * javap -s -p jni.out.production.jni.cn.mrxiexie.jni.JNI 获取签名
 *
 * @author mrxiexie
 * @date 5/14/19 5:40 PM
 */
public class JNI {

    static {
        try {
            // 此处即为本地方法所在链接库名
            System.loadLibrary("JNI");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Cannot load JNI library:\n " + e.toString());
        }
    }

    public static native double[] sqrt(int[] nums);

    public static native int max();

    public native int min();

    public static void log(String msg) {
        System.out.println("In C " + msg);
    }

    public static int[] getNums(int count) {
        int[] nums = new int[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            nums[i] = random.nextInt(count);
        }
        System.out.println("In Java getNums ： " + Arrays.toString(nums));
        return nums;
    }

    public static void main(String[] args) {
        JNI jni = new JNI();
        int max = max();
        System.out.println("In Java max num is " + max);
        int min = jni.min();
        System.out.println("In Java min num is " + min);
        int[] nums = JNI.getNums(10);
        double[] sqrt = sqrt(nums);
        System.out.println("In Java sqrt num is " + Arrays.toString(sqrt));
    }
}