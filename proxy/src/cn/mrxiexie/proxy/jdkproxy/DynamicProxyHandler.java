package cn.mrxiexie.proxy.jdkproxy;

import cn.mrxiexie.proxy.IProxyClass;
import cn.mrxiexie.proxy.ProxyClassImpl;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK InvocationHandler实现类
 *
 * @author mrxiexie
 * @date 5/4/19 9:21 PM
 */
public class DynamicProxyHandler implements InvocationHandler {

    private Object proxied;

    /**
     * @param proxied 被代理对象
     */
    public DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    /**
     * 生成代理对象
     */
    public Object newProxyInstance() {
        return Proxy.newProxyInstance(proxied.getClass().getClassLoader(), proxied.getClass().getInterfaces(), this);
    }

    /**
     * 代理对象调用的方法
     *
     * @param proxy  代理对象
     * @param method 代理方法
     * @param args   方法参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 将代理对象生成字节码到F盘上，方便反编译出 java 文件查看，实际动态代理是不需要自己生成的
        String path = System.getProperty("user.dir") + File.separator + "proxy" + File.separator + "$Proxy0.class";
        addClassToDisk(proxy.getClass().getName(), ProxyClassImpl.class, path);
        // 该设置用于输出jdk动态代理产生的类
        // System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        System.out.println("before");
        System.out.println("被调用的方法名 " + method.getName());
        // 调用真正的方法
        Object object = method.invoke(proxied, args);
        System.out.println("after");
        return object;
    }

    /**
     * 用于生成代理对象的字节码，并将其保存到硬盘上
     */
    private void addClassToDisk(String className, Class<?> cl, String path) {
        // 用于生成代理对象的字节码
        byte[] classFile = ProxyGenerator.generateProxyClass(className, cl.getInterfaces());
        try (FileOutputStream out = new FileOutputStream(path)) {

            // 将代理对象的 class 字节码写到硬盘上
            out.write(classFile);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ProxyClassImpl proxied = new ProxyClassImpl();
        // 生成代理对象
        IProxyClass proxy = (IProxyClass) new DynamicProxyHandler(proxied).newProxyInstance();
        // 通过代理类对象调用代理类方法，实际上会转到 invoke 方法调用
        proxy.doSomething(5);
        proxy.equals(proxied);
        proxy.hashCode();
        proxy.toString();
    }
}
