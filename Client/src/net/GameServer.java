package net;

import entity.NPC_Player2;
import entity.Player;
import entity.PlayerMP;
import main.GamePanel;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


//00 for login
//01 for logout


public class GameServer extends Thread{
    private DatagramSocket socket;

    private GamePanel game;
    private PlayerMP player1 = null;
    private PlayerMP player2 = null;
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

    public GameServer(){

        try {
            this.socket = new DatagramSocket(1331);
        } catch(SocketException e){
            e.printStackTrace();
        }
    }

    public void run(){
        Thread heartbeatThread = new Thread(() -> {
            while (true) {
                System.out.println("Server running");
                //print ips from players connected
                for (  PlayerMP player : connectedPlayers) {
                    System.out.println("["+player.ipAddress.getHostAddress()+"] port: "+player.port);
                }
                try {
                    Thread.sleep(5000); // Sleep for 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        heartbeatThread.start();

        while(true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch(IOException e){
                e.printStackTrace();
            }
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

            //String message = new String(packet.getData(),0, packet.getLength());
            //System.out.println("CLIENT ["+packet.getAddress().getHostAddress()+"] > " + message);
            //if(message.trim().equalsIgnoreCase("ping")){
            //    System.out.println("SERVER > pong");
            //    sendData("pong".getBytes(), packet.getAddress(), packet.getPort());

        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        System.out.println("Parsing packet");
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0,2));
        switch (type){
            default:
            case INVALID:
                break;
            case LOGIN:

                if(connectedPlayers.size() == 0){
                    player1 = new PlayerMP(address, port, '0',0,0,"down");
                    connectedPlayers.add(player1);
                    System.out.println( "LOGIN player 1 from ["+player1.ipAddress.getHostAddress()+"] port: "+player1.port);
                    break;
                }
                if(connectedPlayers.size() == 1){
                    player2 = new PlayerMP(address, port, '0',0,0,"down");
                    connectedPlayers.add(player2);
                    System.out.println( "LOGIN player 2 from ["+player2.ipAddress.getHostAddress()+"] port: "+player2.port);
                    break;
                }
                System.out.println("Server full");



                break;
            case DISCONNECT:
                break;
            case MOVE:
                Packet02Move p = new Packet02Move(data);
                System.out.println("["+address.getHostName()+"] port: "+port+", entity" + p.getEntityID() + " moved to " + p.getX() + "," + p.getY() + " direction: " + p.getDirection());

                sendDataToAllClientsExceptOne(p.getData(), address, port);

                break;
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        System.out.println("Sending to all clients");
        for(PlayerMP player : connectedPlayers){
            sendData(data, player.ipAddress, player.port);
        }
    }

    public void sendDataToAllClientsExceptOne(byte[] data, InetAddress ipAddress, int port) {
        System.out.println("Sending to all clients except one");

        for(PlayerMP player : connectedPlayers){
            if((player.port != port) || (player.ipAddress != ipAddress)){
                sendData(data, player.ipAddress, player.port);
                System.out.println("Sending to: "+player.ipAddress.getHostAddress()+" port: "+player.port);
            }
        }
    }
}
