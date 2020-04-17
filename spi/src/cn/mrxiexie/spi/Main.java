package cn.mrxiexie.spi;

import java.util.ServiceLoader;

/**
 * @author mrxiexie
 * @date 9/10/19 9:30 PM
 */
public class Main {

    public static void main(String[] args) {
        ServiceLoader<ServiceProviderInterface> interfaces =
                ServiceLoader.load(ServiceProviderInterface.class);
        for (ServiceProviderInterface serviceProviderInterface : interfaces) {
            System.out.println(serviceProviderInterface.getName());
        }
    }
}
