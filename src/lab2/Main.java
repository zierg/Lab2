package lab2;

import java.net.*;
//import java.io.*;

public class Main {
    private static int serverPort = 998;
    private static int clientPort = 999;
    private static int bufferSize = 1024;
    private static DatagramSocket ds;
    private static byte[] buffer = new byte[bufferSize];
    
    private static void theServer() throws Exception {
        int pos = 0;
        while (true) {
            int c = System.in.read();
            switch (c) {
                case -1: {
                    System.out.println("Work complete");
                    ds.close();
                    return;
                }
                case '\r': {
                    break;
                }
                case '\n': {
                    ds.send(new DatagramPacket(buffer, pos, InetAddress.getLocalHost(), clientPort));
                    pos = 0;
                    break;
                }
                default: {
                    buffer[pos++] = (byte) c;
                }
            }
        }
    }
    
    private static void theClient() throws Exception {
        while (true) {
            DatagramPacket p = new DatagramPacket(buffer, buffer.length);
            ds.receive(p);
            System.out.println(new String(p.getData(), 0, p.getLength()));
        }
    }
    
    public static void main(String[ ] args) throws Exception {
        if (args.length == 1) {
            ds = new DatagramSocket(serverPort);
            theServer();
        } else {
            ds = new DatagramSocket(clientPort);
            theClient();
        }
    }
}
