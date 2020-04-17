package cn.mrxiexie.proxy.cglibproxy;

/**
 * 被代理类
 *
 * @author mrxiexie
 * @date 5/14/19 2:16 PM
 */
public final class FinalCglibProxyClass {

    public void doSomething(Object o) {
        System.out.println("我是被代理类 : " + o);
    }
}
