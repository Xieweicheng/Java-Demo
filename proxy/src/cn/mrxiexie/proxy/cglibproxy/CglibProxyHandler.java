package cn.mrxiexie.proxy.cglibproxy;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Cglib MethodInterceptor 实现类
 *
 * @author mrxiexie
 * @date 5/4/19 9:52 PM
 */
public class CglibProxyHandler implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    /**
     * 代理对象调用的方法
     *
     * @param o           代理对象
     * @param method      被拦截的方法
     * @param args        被拦截的方法参数
     * @param methodProxy 用于调用非拦截方法，可以根据需要调用多次 {@link MethodProxy#invokeSuper(Object, Object[])}
     * @return cglib 生成用来代替 Method 对象的一个对象，使用 MethodProxy 比调用 JDK 自身的 Method 直接执行方法效率会有提升
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {

        System.out.println("start " + methodProxy.getSuperName());
        System.out.println("被调用的方法名 " + method.getName());
        Object o1 = methodProxy.invokeSuper(o, args);
        //Object o2 = method.invoke(o, args); 使用这种方式会发生死循环，因为方法会被拦截
        System.out.println("after " + methodProxy.getSuperName());
        return o1;
    }

    /**
     * 生成代理对象
     */
    public Object newProxyInstance(Class<?> c) {
        // 设置代理类的父类，即被代理类
        enhancer.setSuperclass(c);
        // 设置 CallBack 接口的实例
        enhancer.setCallback(this);
        // 使用默认无参数的构造函数创建目标对象
        return enhancer.create();
    }

    public static void main(String[] args) {
        // 该设置用于输出cglib动态代理产生的类
        String path = System.getProperty("user.dir") + File.separator + "proxy" + File.separator + "proxy";
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, path);
        // 该设置用于输出jdk动态代理产生的类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        // 使用 final 的类会出错，这是因为 cglib 的动态代理是通过继承来实现的
//        FinalCglibProxyClass proxy = (FinalCglibProxyClass) new CglibProxyHandler().newProxyInstance(FinalCglibProxyClass.class);
        CglibProxyClass proxy = (CglibProxyClass) new CglibProxyHandler().newProxyInstance(CglibProxyClass.class);
        proxy.doOtherSomething(5);
        proxy.doSomething(5);
        proxy.equals(proxy);
        proxy.hashCode();
        proxy.toString();
    }
}
