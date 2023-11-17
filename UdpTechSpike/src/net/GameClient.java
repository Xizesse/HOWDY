package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;  // Import statement for InetAddress
import java.net.SocketException;
import java.net.UnknownHostException;
public class GameClient extends Thread {

    private DatagramSocket socket;
    private InetAddress ipAddress;

    public GameClient(String ipAddress) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        this.ipAddress = InetAddress.getByName(ipAddress);
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("SERVER > " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendData(String message) {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            GameClient client = new GameClient("localhost"); // Use the server's IP if not running on localhost
            client.start();
            client.sendData("ping");

            // Optionally, you can sleep the main thread for a while to prevent the client from exiting immediately
            try {
                Thread.sleep(10000); // Sleep for 10 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}