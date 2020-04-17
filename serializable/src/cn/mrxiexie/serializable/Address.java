package cn.mrxiexie.serializable;

import java.io.*;

/**
 * {@link #writeObject(ObjectOutputStream)}
 * {@link #readObject(ObjectInputStream)}
 * {@link #writeReplace()}
 * {@link #readResolve()}
 *
 * @author mrxiexie
 * @date 5/1/19 8:34 PM
 */
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
            // 重置 district 为 null
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public static String getDistrict() {
        return district;
    }

    public static void setDistrict(String district) {
        Address.district = district;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
