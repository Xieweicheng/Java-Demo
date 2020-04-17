package cn.mrxiexie.concurrent.basic;

/**
 * @author mrxiexie
 * @date 19-12-12 下午5:14
 */
public class ThreadLocal {

    private static java.lang.ThreadLocal<Long> threadLocal = new java.lang.ThreadLocal<>();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                threadLocal.set(System.currentTimeMillis());
                ThreadUtils.second(10);
                ThreadUtils.log(threadLocal.get() + "");
                threadLocal.remove();
            });
            thread.setName(i + "");
            thread.start();
        }

    }
}
