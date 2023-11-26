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

    private GamePanel game = new GamePanel();

    private List<PlayerMP> connectedPlayers = new ArrayList<>();




    public GameServer(){

        try {
            this.socket = new DatagramSocket(1331);
        } catch(SocketException e){
            e.printStackTrace();
        }
    }

    public void run(){
        game.setupGame();
        Thread heartbeatThread = new Thread(() -> {
            while (true) {
                System.out.println("Server running");
                //print ips from players connected
                for (  PlayerMP player : connectedPlayers) {
                    if(player == null){
                        continue;
                    }
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

        //initialize 2 elements in connectedPlayers
        connectedPlayers.add(null);
        connectedPlayers.add(null);

        while(true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch(IOException e) {
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
        //System.out.println("Parsing packet");
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0,2));
        switch (type){
            default:
            case INVALID:
                break;
            case LOGIN:

                if(connectedPlayers.get(0) == null){
                    connectedPlayers.set(0, new PlayerMP(address, port, '0',0,0,"down"));
                    System.out.println( "LOGIN player 1 from ["+connectedPlayers.get(0).ipAddress.getHostAddress()+"] port: "+connectedPlayers.get(0).port);
                    break;
                }
                if(connectedPlayers.get(1) == null){
                    connectedPlayers.set(1, new PlayerMP(address, port, '0',0,0,"down"));
                    System.out.println( "LOGIN player 2 from ["+connectedPlayers.get(1).ipAddress.getHostAddress()+"] port: "+connectedPlayers.get(1).port);
                    break;

                }
                System.out.println("Server full");



                break;


            case DISCONNECT:
                break;

            case MOVE:
                Packet02Move p = new Packet02Move(data);
                updatePlayer(address, port, p.getX(), p.getY(), p.getDirection());
                //System.out.println("["+address.getHostName()+"] port: "+port+", entity" + p.getEntityID() + " moved to " + p.getX() + "," + p.getY() + " direction: " + p.getDirection());
                sendDataToAllClientsExceptOne(p.getData(), address, port);
                break;

            case OBJECT:
                Packet04Object p4 = new Packet04Object(data);
                System.out.println("["+address.getHostName()+"] port: "+port+", object" + (char)p4.getItemID() );
                //check if that item is available
                if (game.obj[p4.getItemID()] != null) {
                    //check if the item is already given
                    //send packet back to the player -> he recieves the item
                    System.out.println("["+address.getHostName()+"] port: "+port+", object" + (char) p4.getItemID() + " give");
                    Packet04Object p4_2 = new Packet04Object(p4.getItemID(), true);
                    sendData(p4_2.getData(), address, port);
                    //send packet back to the other player -> he does not recieve the item
                    System.out.println("["+address.getHostName()+"] port: "+port+", object" +  p4.getItemID() + " already given");
                    Packet04Object p4_3 = new Packet04Object(p4.getItemID(), false);
                    sendDataToAllClientsExceptOne(p4_3.getData(), address, port);
                    break;

                }

                break;
        }

        //print player coordinates as player 1 and 2
        if(connectedPlayers.get(0) != null){
            System.out.println("Player 1: "+connectedPlayers.get(0).x+","+connectedPlayers.get(0).y);
        }
        if(connectedPlayers.get(1) != null){
            System.out.println("Player 2: "+connectedPlayers.get(1).x+","+connectedPlayers.get(1).y);
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
        //System.out.println("Sending to all clients");
        for(PlayerMP player : connectedPlayers){
            if(player == null){
                continue;
            }
            sendData(data, player.ipAddress, player.port);
        }
    }

    public void sendDataToAllClientsExceptOne(byte[] data, InetAddress ipAddress, int port) {
        //System.out.println("Sending to all clients except one");

        for(PlayerMP player : connectedPlayers){
            if(player == null){
                continue;
            }
            if((player.port != port) && (player.ipAddress != ipAddress)){
                sendData(data, player.ipAddress, player.port);
                System.out.println("Sending to: "+player.ipAddress.getHostAddress()+" port: "+player.port);
            }
        }
    }

    private void updatePlayer(InetAddress address, int port, int x, int y, String direction) {
        for (PlayerMP player : connectedPlayers) {
            if (player != null && player.ipAddress.equals(address) && player.port == port) {
                player.x = x;
                player.y = y;
                player.direction = direction;
                //System.out.println("Player updated: " + player.x + "," + player.y);
                return;
            }
        }
        //System.out.println("Player not found for update.");
    }
}
