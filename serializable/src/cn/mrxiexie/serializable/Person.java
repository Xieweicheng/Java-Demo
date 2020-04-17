package cn.mrxiexie.serializable;

import java.io.*;

/**
 * {@link Serializable} 演示
 *
 * @author mrxiexie
 * @date 4/30/19 2:18 PM
 */
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Person.password = password;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                '}';
    }
}
