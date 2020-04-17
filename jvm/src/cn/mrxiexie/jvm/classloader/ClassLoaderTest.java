package cn.mrxiexie.jvm.classloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author mrxiexie
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        Class<?> aClass = new HotClassLoader().findClass("cn.mrxiexie.jvm.classloader.ClassLoaderTest");
        System.out.println(aClass);


    }

    public void test() throws Exception {
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }
            }
        };
        Object obj = myLoader.loadClass("cn.mrxiexie.jvm.classloader.ClassLoaderTest").newInstance();
        System.out.println(obj.getClass());
        System.out.println(obj instanceof ClassLoaderTest);
    }
}
