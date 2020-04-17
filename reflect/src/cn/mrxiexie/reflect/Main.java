package cn.mrxiexie.reflect;

import org.junit.Test;

import java.lang.reflect.AnnotatedType;

import cn.mrxiexie.reflect.son.ISon;
import cn.mrxiexie.reflect.son.Son;
import cn.mrxiexie.reflect.util.Log;

public class Main {

    private Son son = new Son();
    private Class<? extends Son> clazz = son.getClass();

    @Test
    public void test() {
        Log.info("", clazz.getCanonicalName());
        Log.info("", clazz.getSimpleName());
        Log.info("", clazz.getTypeName());
        Log.info("", clazz.getName());
        Log.info("Son getComponentType", clazz.getComponentType());
        Log.info("Son getAnnotatedInterfaces", clazz.getAnnotatedInterfaces());
        Log.info("Son getAnnotatedSuperclass", clazz.getAnnotatedSuperclass());
    }

    @Test
    public void clazz() {
        ISon iSon = new ISon() {

        };
        Son.PublicInnerSon publicInnerSon = son.new PublicInnerSon();

        System.out.println(clazz.getSimpleName());
        Log.info("Son getClass", clazz);
        Log.info("Son getClasses", "获取所有公共内部类和静态内部类");
        Log.info("", clazz.getClasses());
        Log.info("Son getDeclaredClasses", "获取自身所有内部类和静态内部类");
        Log.info("", clazz.getDeclaredClasses());
        Log.info("getDeclaringClass & getEnclosingClass ", "获取申明类（匿名内部类获取不到）& 获取直接外部类");
        Log.info("iSon     getDeclaringClass", iSon.getClass().getDeclaringClass());
        Log.info("innerSon getDeclaringClass", publicInnerSon.getClass().getDeclaringClass());
        Log.info("iSon     getEnclosingClass", iSon.getClass().getEnclosingClass());
        Log.info("innerSon getEnclosingClass", publicInnerSon.getClass().getEnclosingClass());
    }

    @Test
    public void field() {
        Log.info("Son getFields", "获取所有公共字段");
        Log.info("", clazz.getFields());
        Log.info("Son getDeclaredFields", "获取自身所有字段");
        Log.info("", clazz.getDeclaredFields());
    }

    @Test
    public void method() {
        ISon iSon = new ISon() {

        };
        class A {}

        Log.info("Son getMethods", "获取所有公共方法");
        Log.info("", clazz.getMethods());
        Log.info("Son getDeclaredMethods", "获取自身所有方法");
        Log.info("", clazz.getDeclaredMethods());
        Log.info("getEnclosingMethod", "获取声明该类的方法");
        Log.info("ison getEnclosingMethod", iSon.getClass().getEnclosingMethod());
        Log.info("A    getEnclosingMethod", A.class.getEnclosingMethod());
    }

    @Test
    public void annotation() {
        Log.info("Son getAnnotations", "获取所有注解（包含父类继承下来的注解 @AllAnnotation，接口的注解不会继承）");
        Log.info("Son getAnnotations", clazz.getAnnotations());
        Log.info("Son getDeclaredAnnotations", "获取自身所有注解");
        Log.info("Son getDeclaredAnnotations", clazz.getDeclaredAnnotations());
    }

    @Test
    public void interfaces() {
        //TODO
        Class<? extends Son> clazz = Son.class;
        Log.info("Son getInterfaces", clazz.getInterfaces());
        Log.info("Son getGenericInterfaces", clazz.getGenericInterfaces());
        for(AnnotatedType annotatedInterface : clazz.getAnnotatedInterfaces()) {
            Log.info("AnnotatedType getAnnotations", annotatedInterface.getAnnotations());
            Log.info("AnnotatedType getDeclaredAnnotations", annotatedInterface.getDeclaredAnnotations());
            Log.info("AnnotatedType getType", annotatedInterface.getType());
        }
    }
}
