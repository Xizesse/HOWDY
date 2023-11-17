package net;

import main.GamePanel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GameServer {
    private DatagramSocket socket;
    private GamePanel game;

    public GameServer(GamePanel game){
        this.game = game;
        try {
            this.socket = new DatagramSocket(1331);
        } catch(SocketException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch(IOException e){
                e.printStackTrace();
            }
            String message = new String(packet.getData());
            System.out.println("CLIENT > " + message);
            if(message.equalsIgnoreCase("ping")){
                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
            }
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
