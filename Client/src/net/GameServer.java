package net;

import entity.Player;
import entity.PlayerMP;
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


    private List<ClientInfo> connectedPlayers = new ArrayList<ClientInfo>();

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
                for (  ClientInfo clientInfo : connectedPlayers) {
                    System.out.println("["+clientInfo.ipAddress.getHostAddress()+"] port: "+clientInfo.port);
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

                ClientInfo clientInfo = new ClientInfo(address, port);
                System.out.println( "LOGIN from ["+clientInfo.ipAddress.getHostAddress()+"] port: "+clientInfo.port);
                connectedPlayers.add(clientInfo);
                if(/*connectedPlayers.size() ==1*/ true){
                    Packet00Login p = new Packet00Login();
                    p.writeData(this);
                }


                break;
            case DISCONNECT:
                break;
            case MOVE:
                Packet02Move p = new Packet02Move(data);
                System.out.println("["+address.getHostAddress()+"] port: "+port+" > MOVE: "+p.getX()+","+p.getY());
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
        for(ClientInfo clientInfo : connectedPlayers){
            sendData(data, clientInfo.ipAddress, clientInfo.port);
        }
    }

    public void sendDataToAllClientsExceptOne(byte[] data, InetAddress ipAddress, int port) {
        System.out.println("Sending to all clients except one");

        for(ClientInfo clientInfo : connectedPlayers){
            if((clientInfo.port != port) || (clientInfo.ipAddress != ipAddress)){
                sendData(data, clientInfo.ipAddress, clientInfo.port);
                System.out.println("Sending to: "+clientInfo.ipAddress.getHostAddress()+" port: "+clientInfo.port);
            }
        }
    }
}
