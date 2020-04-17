package cn.mrxiexie.reflect.util;

import java.util.Arrays;

public class Log {

    public static void info(String desc, Object text) {
        System.out.println(String.format("%-40s - %s", desc, text));
    }

    public static void info(String desc, Object[] objects) {
        if(objects.length == 0) {
            info(desc, Arrays.toString(objects));
            return;
        }
        info(desc, "start");
        for(Object object : objects) {
            info("", object);
        }
        info(desc, "end");
    }
}
