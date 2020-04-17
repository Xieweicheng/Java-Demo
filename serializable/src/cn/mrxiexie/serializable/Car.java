package cn.mrxiexie.serializable;

import java.io.*;
import java.math.BigDecimal;

/**
 * {@link Externalizable} 演示
 *
 * @author mrxiexie
 * @date 4/30/19 3:04 PM
 */
public class Car implements Externalizable {

    private String name;

    private transient String brand;

    private static BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public static BigDecimal getPrice() {
        return price;
    }

    public static void setPrice(BigDecimal price) {
        Car.price = price;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                '}';
    }

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
        String filePath = "/home/mrxiexie/Desktop/car.ser";
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Car car = new Car();
            car.setName("MrXieXie");
            car.setBrand("MrXieXie");
            Car.setPrice(new BigDecimal("1234567.89"));
            // 序列化到文件中
            objectOutputStream.writeObject(car);
            // 重置 price 为 null
            Car.setPrice(null);
            // 反序列化到内存中
            car = (Car) objectInputStream.readObject();
            System.out.println(car);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
