package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;  // Import statement for InetAddress
import java.net.SocketException;
public class GameServer extends Thread {

    private DatagramSocket socket;

    public GameServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("CLIENT > " + message);
                if ("ping".equalsIgnoreCase(message.trim())) {
                    sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 1332;
        try {
            Thread server = new GameServer(port);
            server.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}