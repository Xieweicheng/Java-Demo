身为一枚程序员，多多少少会听过**代理**（Proxy）这个词，然而什么是代理呢？

简单介绍一下生活中常见的代理模式：

> 我们需要购买国外的产品时，我们可以坐飞机去国外某店购买，也可以去**代购**中购买，此时**代购**就是国外某店的代理，当我们购买到产品时，实际上是先购买请求发送给代购，代购再发送给某店，某店返回购买成功给代购，代购再返回成功给我们。某店我们可以理解为委托类（被代理类），代购可以理解为代理类。

那么使用**代理模式**有什么优点呢？

- 解耦**调用者**和**被代理类**，在不修改**被代理类**的前提下，对其进行逻辑控制

其缺点也很明显

- 由于**调用者**和**被代理类**之间添加了**代理类**，因此有些类型的代理模式可能会造成请求的处理速度变慢，而且**代理类**一般会有额外的工作，有些代理模式的实现非常复杂

在 Java 中代理可以分为**静态代理**和**动态代理**，下面我们来详解这两种代理的区别

### 静态代理

代理类在程序运行前就已经存在，这种代理模式称为**静态代理**。一般情况下，**代理类**和**被代理类**都会实现同一接口。

下面我们创建统一接口 `IProxyClass`：

```java
public interface IProxyClass {
    void doSomething(Object o);
}
```

创建**被代理类** `ProxyClassImpl`：

```java
public class ProxyClassImpl implements IProxyClass {

    @Override
    public void doSomething(Object o) {
        System.out.println("我是被代理类 : " + o);
    }
}
```

创建**代理类** `StaticProxyHandler`：

```java
public class StaticProxyHandler implements IProxyClass {

    private IProxyClass iProxyClass;

    public StaticProxyHandler(IProxyClass iProxyClass) {
        this.iProxyClass = iProxyClass;
    }

    @Override
    public void doSomething(Object o) {
        IProxyClass.doSomething(o);
    }

    public static void main(String[] args) {
        new StaticProxyHandler(new ProxyClassImpl()).doSomething(5);
    }
}
```

若现在我们需要记录 `doSomething` 的运行时间，或者添加某些判断要满足条件才执行，我们则不需修改**被代理类**，只需在**代理类**上做修改：

```java
public class StaticProxyHandler implements IProxyClass {

    、、、
    
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
}
```

从表面上，已经解决了我们的需求，但现在需求又变动了，要在该类的所有方法都添加同样的逻辑，若该类有 100 个方法，那我们只能当个 **CV工程师** 了

由此可以看出**静态代理**的局限性，当类有多个方法时，重复的逻辑要反复实现

程序员往往是很懒的，连 **CV工程师** 也不愿意当，**动态代理**就这么诞生了

### 动态代理

顾名思义，动态代理就是在程序运行的时候动态生成的。生成动态代理的方法有两种，`JDK`自带的动态代理，还有第三方的`CGLIB`，下面我们分别对这两种方式进行实现。

#### JDK动态代理

使用**JDK动态代理**需要用到 `InvocationHandler` 接口，创建**调用处理器** `DynamicProxyHandler` 如下：

```java
public class DynamicProxyHandler implements InvocationHandler {

    private Object proxied;

    /**
     * @param proxied 被代理对象
     */
    public DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    /**
     * 返回代理对象
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
        String path = System.getProperty("user.dir") + File.separator + "proxy" + File.separator + "$Proxy0.class";
        addClassToDisk(proxy.getClass().getName(), ProxyClassImpl.class, path);
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
```

`Proxy.newProxyInstance(loader, interfaces, h)`方法用于创建代理对象
- loader：定义了代理类的ClassLoader
- interfaces：代理类实现的接口列表 
- h：调用处理器

`invoke(proxy, method, args)`方法为代理对象实际调用的方法
- proxy：代理对象
- method：被代理的方法
- args：被代理的方法参数

`method.invoke(obj, args)`调用真正被代理对象的方法
- obj：被代理对象
- args：被代理的方法参数

main 方法执行结果如下，从结果可以看出，`equals`、`hashCode` 和 `toString`三个方法都被动态代理了

```
before
被调用的方法名 doSomething
我是被代理类 : 5
after
before
被调用的方法名 equals
after
before
被调用的方法名 hashCode
after
before
被调用的方法名 toString
after
```

`addClassToDisk`方法把代理类的字节码保存在了硬盘上，字节码如下：

```java
public final class $Proxy0 extends Proxy implements IProxyClass {
    private static Method m1;
    private static Method m3;
    private static Method m2;
    private static Method m0;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final void doSomething(Object var1) throws  {
        try {
            super.h.invoke(this, m3, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int hashCode() throws  {
        try {
            return (Integer)super.h.invoke(this, m0, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m3 = Class.forName("cn.mrxiexie.proxy.IProxyClass").getMethod("doSomething", Class.forName("java.lang.Object"));
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
```

从代理类字节码可以看出，代理类继承了 `Proxy` 类并实现了我们的统一接口 `IProxyClass`，在静态代码块中，使用了反射把接口中的方法和三个 Object 内的方法实现出来，在构造方法中传入了我们的编写的 `DynamicProxyHandler` 类的对象给父类 `Proxy`，调用代理类的方法，实际上是调用父类 `Proxy` 的 `InvocationHandler`（即构造方法传入的`DynamicProxyHandler`） 的 `invoke` 方法，并把代理对象本身，反射得到对应的方法对象和方法参数传入。

现在无论是需要记录方法的运行时长，还是记录日志等操作都可以在 `DynamicProxyHandler` 的 `invoke` 中完成。

但是 **JDK动态代理** 是基于接口的，如果我们需要动态代理的类没有实现接口，我们该如何处理呢？这时候我们就需要用到 **CGLIB动态代理**

#### CGLIB动态代理

**CGLIB** (Code Generation Library) 是一个基于 **ASM** 的字节码生成库，它允许我们在运行时对字节码进行修改和动态生成。**CGLIB** 通过继承方式实现代理。

导入`asm` 和 `cglib` 的 jar 包，或使用 `gradle` 引入以下依赖

```groovy
// https://mvnrepository.com/artifact/cglib/cglib
implementation group: 'cglib', name: 'cglib', version: '3.2.12'
// https://mvnrepository.com/artifact/org.ow2.asm/asm
implementation group: 'org.ow2.asm', name: 'asm', version: '7.1'
```

先创建不实现任何接口的**被代理类**：

```java
public class CglibProxyClass {
    public void doSomething(Object o) {
        System.out.println("我是被代理类 : " + o);
    }
    public final void doOtherSomething(Object o){
        System.out.println("我是 final 方法 : " + o);
    }
}
```

**CGLIB动态代理** 需要使用 `Enhancer`

 ```java
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

        // 使用 final 的类会出错，这是因为 cglib 的动态代理是通过继承来实现的
        // FinalCglibProxyClass proxy = (FinalCglibProxyClass) new CglibProxyHandler().newProxyInstance(FinalCglibProxyClass.class);
        CglibProxyClass proxy = (CglibProxyClass) new CglibProxyHandler().newProxyInstance(CglibProxyClass.class);
        proxy.doSomething(5);
        proxy.equals(proxy);
        proxy.hashCode();
        proxy.toString();
    }
}
 ```

`enhancer.create()`使用无参构造器创建代理对象，调用该方法前需指定代理类字节码和指定回调接口，如下：

- `enhancer.setSuperclass(superclass)`：指定父类的字节码，即代理类字节码
- `enhancer.setCallback(callback)`：指定回调，`Callback` 是一个空接口，在 `cglib` 中有以下 6 种实现，这里我们使用的最为强大的 `MethodInterceptor` 接口，其用法类似与 **JDK动态代理** 中的 `InvocationHandler`
  - `MethodInterceptor`
  - `NoOp`
  - `LazyLoader`
  - `Dispatcher`
  - `InvocationHandler`
  - `FixedValue`

生成的字节码跟 **JDK动态代理** 类似，不过 `cglib` 使用的是继承，并实现 `Factory` 接口：

```java
public class CglibProxyClass$$EnhancerByCGLIB$$e0eebff4 extends CglibProxyClass implements Factory {
    、、、
    final void CGLIB$doSomething$0(Object var1) {
        super.doSomething(var1);
    }

    public final void doSomething(Object var1) {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (var10000 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        if (var10000 != null) {
            var10000.intercept(this, CGLIB$doSomething$0$Method, new Object[]{var1}, CGLIB$doSomething$0$Proxy);
        } else {
            super.doSomething(var1);
        }
    }
    、、、
}
```

main 方法执行结果如下，从结果可以看出，`equals`、`hashCode` 和 `toString`三个方法都被动态代理了

```
我是 final 方法 : 5
start CGLIB$doSomething$0
被调用的方法名 doSomething
我是被代理类 : 5
after CGLIB$doSomething$0
start CGLIB$equals$1
被调用的方法名 equals
after CGLIB$equals$1
start CGLIB$hashCode$3
被调用的方法名 hashCode
after CGLIB$hashCode$3
start CGLIB$toString$2
被调用的方法名 toString
start CGLIB$hashCode$3
被调用的方法名 hashCode
after CGLIB$hashCode$3
after CGLIB$toString$2
```

需要注意的是，`CGLIB动态代理`使用的是继承的方式去实现动态代理，若被代理类为 `final`，不能被继承，运行时则会抛出 `java.lang.IllegalArgumentException: Cannot subclass final class cn.mrxiexie.proxy.cglibproxy.FinalCglibProxyClass` 异常，而且 `final` 方法犹豫不能被重载，所以不会被代理。



