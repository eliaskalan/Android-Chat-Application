package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Random;
import java.util.Scanner;

import controller.Address;

public class Config {
    public static String SAVED_FILES_PATH = "C:/Users/elias/Documents/";
    public static final String IMAGE_TYPE="image";
    public static final String MESSAGE_TYPE="message";
    public static String ZOOKEEPER_IP = "192.168.1.8";

    public static Address ZOOKEEPER_CLIENTS = new Address(ZOOKEEPER_IP, 22346);




    public static final String EXIT_FROM_TOPIC = "EXIT";

    public static String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public static void sendAMessage(BufferedWriter bufferedWriter, String message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Problem to send message ~ string");
            throw new RuntimeException(e);
        }

    }

    public static String readFromUser(String message){
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLine();
    }
    public static String getPublicIp() {
        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    if (addr instanceof Inet6Address) continue;

                    ip = addr.getHostAddress();
                    System.out.println(ip);
                    return ip;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return "localhost";
    }
}
