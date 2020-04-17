package cn.mrxiexie.proxy.staticproxy;

import cn.mrxiexie.proxy.ProxyClassImpl;
import cn.mrxiexie.proxy.IProxyClass;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 静态代理类
 *
 * @author mrxiexie
 * @date 5/4/19 12:15 AM
 */
public class StaticProxyHandler implements IProxyClass {

    private IProxyClass iProxyClass;

    public StaticProxyHandler(IProxyClass iProxyClass) {
        this.iProxyClass = iProxyClass;
    }

    @Override
    public void doSomething(Object o) {
        if (otherCondition()) {
            LocalDateTime begin = LocalDateTime.now();
            System.out.println("嘿嘿！我是代理类");
            iProxyClass.doSomething(o);
            LocalDateTime end = LocalDateTime.now();
            System.out.println("方法执行时长为：" + Duration.between(begin, end).toMillis());
        }
    }

    private boolean otherCondition() {
        return true;
    }

    public static void main(String[] args) {
        new StaticProxyHandler(new ProxyClassImpl()).doSomething(5);
    }
}
