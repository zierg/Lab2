package lab2;

import java.net.*;

public class Main {
    public static void main(String[ ] args) throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        System.out.println(address);
        
        address = InetAddress.getByName("yandex.ru");
        System.out.println(address);
    }
}
