在程序运行的过程中，所有的对象都是保存在内存中的，这意味着当程序运行结束的时候，操作系统会把分配给对象的内存全部回收。

那有什么办法可以把对象存储起来？

序列化就可以帮我们做到，序列化是将对象的状态信息转换成可取的格式的过程，可以存储为文件，或者透过网络发送数据时进行编码的过程，可以是字节或是XML等格式。其相反的过程就被称为反序列化。

Java中准备了 `Serializable`、`Externalizable`两个接口（需要序列化的类必须实现这两个接口其一），和 `ObjectOutputStream`、`ObjectInputStream`两个类来帮助开发者实现序列化与反序列化。

### Serializable Code

只需在需要序列化的类上实现 `Serializable` 接口，即可通过 `ObjectOutputStream` 实现序列化到文件系统中。

```java
@Data
public class Person implements Serializable {

    private String name;

    private transient Integer age;

    private static String password;

    public static void main(String[] args) {

        String filePath = "/home/mrxiexie/Desktop/person.ser";
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Person person = new Person();
            person.setName("MrXieXie");
            person.setAge(23);
            Person.setPassword("123456");
            // 序列化到文件中
            objectOutputStream.writeObject(person);
            // 重置 password 为 null
            Person.setPassword(null);
            // 反序列化到内存中
            person = (Person) objectInputStream.readObject();
            System.out.println(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

输出结果为 `Person{name='MrXieXie', age=null, password='null'}`

**注意：序列化到文件后，将 `password` 的值重置为 `null`。**

**可以看出在 `Serializable`，静态变量还有被 `transient` 标注的变量不会被序列化。**

那么是不是所有的静态变量和被 `transient` 标注的变量就不会被序列化呢？让我们来看一下 `Externalizable` 接口。

### Externalizable Code

使用 `Externalizable` 实现序列化要比 `Serializable` 复杂一丢丢，需要实现 `writeExternal` 和 `readExternal` 方法，这两个方法会在序列化和反序列化时调用。

需要注意的是，该接口需要调用被序列化类的无参构造器，若不提供则会抛出异常 `java.io.InvalidClassException: cn.mrxiexie.serializable.Car; no valid constructor`

```java
@Data
public class Car implements Externalizable {

    private String name;

    private transient String brand;

    private static BigDecimal price;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(brand);
        out.writeObject(price);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        brand = (String) in.readObject();
        price = (BigDecimal) in.readObject();
    }

    public static void main(String[] args) {
        String filePath = "/home/mrxiexie/Desktop/IProxyClass.ser";
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Car IProxyClass = new Car();
            IProxyClass.setName("MrXieXie");
            IProxyClass.setBrand("MrXieXie");
            Car.setPrice(new BigDecimal("1234567.89"));
            // 序列化到文件中
            objectOutputStream.writeObject(IProxyClass);
            // 重置 price 为 null
            Car.setPrice(null);
            // 反序列化到内存中
            IProxyClass = (Car) objectInputStream.readObject();
            System.out.println(IProxyClass);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

输出结果为 `Car{name='MrXieXie', brand='MrXieXie', price=1234567.89}`

**注意：序列化到文件后，将 `price` 的值重置为 `null`。**

**可以看出即使是静态变量和被 `transient` 标注的变量还是会被序列化。原因很简单，`Serializable` 接口是自动序列化，当发现有 `transient` 标注的变量时则不序列化该变量，而 `Externalizable` 接口则是需要在 `writeExternal` 方法中指定需要序列化的变量。**

### More

在 **Serializable** 接口的注释中，提及到以下四个方法，分别是：

- private void writeObject(java.io.ObjectOutputStream out) throws IOException
- private void readObject(java.io.ObjectInputStream in) throws IOException
- private Object readResolve() throws ObjectStreamException
- private Object writeReplace() throws ObjectStreamException

**方法执行顺序：**

在调用 `ObjectOutputStream` 的 `write` 系列方法（序列化）时，会触发 `writeReplace` 和 `writeObject` 方法：

`write -> writeReplace -> writeObject`

在调用 `ObjectInputStream` 的 `read` 系列方法（反序列化）时，会触发 `readObject` 和 `readResolve` 方法：

`read -> readObject -> readResolve`

**方法解释：**

`writeReplace` ：当序列化对象需要一个替代的对象时，可以使用该方法，返回值为替代的对象。

`writeObject`：可以使用该方法指定需要序列化的变量，默认使用 `ObjectOutputStream` 的 `defaultWriteObject` 方法，该方法执行默认的序列化机制（不序列化 `transient` 和 `static` 标注的变量）

`readObject`：可以使用该方法指定需要反序列化的变量，默认使用 `ObjectInputStream` 的 `defaultReadObject` 方法，该方法执行默认的序列化机制（不序列化 `transient` 和 `static` 标注的变量）

`readResolve`：当反序列化需要一个替代的对象时，可以使用该方法，返回值为替代的对象。

**Show Code**

```java
@Data
public class Address implements Serializable {

    private String city;

    private transient String province;

    private static String district;

    public static void main(String[] args) {

        String filePath = "/home/mrxiexie/Desktop/address.ser";

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Address address = new Address();
            address.setCity("佛山市");
            address.setProvince("广东省");
            Address.setDistrict("顺德区");
            // 序列化到文件中
            System.out.println("开始序列化");
            objectOutputStream.writeObject(address);
            System.out.println("结束序列化");
            // 重置 distric 为 null
            Address.setDistrict(null);
            // 反序列化到内存中
            System.out.println("开始反序列化");
            address = (Address) objectInputStream.readObject();
            System.out.println("结束反序列化");
            System.out.println("最终读取到的对象 ： " + address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        System.out.println("Address.writeObject");
        out.writeObject(city);
        out.writeObject(province);
        out.writeObject(district);
//        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        System.out.println("Address.readObject");
        city = (String) in.readObject();
        province = (String) in.readObject();
        district = (String) in.readObject();
//        in.defaultReadObject();
    }

    private void readObjectNoData()
            throws ObjectStreamException {
        System.out.println("Address.readObjectNoData");
    }

    private Object readResolve() throws ObjectStreamException {
        System.out.println("Address.readResolve");
        System.out.println("Address.readResolve : 修改前的对象 [" + this + "]");
        Address address = new Address();
        address.setCity("readResolveCity");
        address.setProvince("readResolveProvince");
        Address.setDistrict("readResolveDistrict");
        return address;
    }

    private Object writeReplace() throws ObjectStreamException {
        System.out.println("Address.writeReplace");
        System.out.println("Address.writeReplace : 修改前的对象 [" + this + "]");
        Address address = new Address();
        address.setCity("writeReplaceCity");
        address.setProvince("writeReplaceProvince");
        Address.setDistrict("writeDistrict");
        return address;
    }
}
```

输出结果为：

```
开始序列化
Address.writeReplace
Address.writeReplace : 修改前的对象 [Address{city='佛山市', province='广东省', district='顺德区'}]
Address.writeObject
结束序列化
开始反序列化
Address.readObject
Address.readResolve
Address.readResolve : 修改前的对象 [Address{city='writeReplaceCity', province='writeReplaceProvince', district='writeDistrict'}]
结束反序列化
最终读取到的对象 ： Address{city='readResolveCity', province='readResolveProvince', district='readResolveDistrict'}
```

