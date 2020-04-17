package cn.mrxiexie.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author mrxiexie
 * @date 9/10/19 11:22 PM
 */
public class Main {

    public static void main(String[] args) throws Throwable {
        getMH(new BananaHandler()).invokeExact("xibalaxi");
    }

    private static MethodHandle getMH(Object service) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = MethodType.methodType(void.class, String.class);
        return MethodHandles.lookup().findVirtual(service.getClass(), "invoke", methodType).bindTo(service);
    }
}
