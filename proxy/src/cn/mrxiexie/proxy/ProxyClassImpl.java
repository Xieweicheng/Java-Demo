package cn.mrxiexie.proxy;

/**
 * 被代理类
 *
 * @author mrxiexie
 * @date 5/4/19 12:14 AM
 */
public class ProxyClassImpl implements IProxyClass {

    @Override
    public void doSomething(Object o) {
        System.out.println("我是被代理类 : " + o);
    }

    public void doOtherSomething(Object o) {
        System.out.println("我是被代理类接口中没有的方法 : " + o);
    }
}
